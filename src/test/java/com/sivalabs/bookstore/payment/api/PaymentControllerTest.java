package com.sivalabs.bookstore.payment.api;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.payment.domain.CreditCard;
import com.sivalabs.bookstore.payment.domain.CreditCardRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

class PaymentControllerTest extends AbstractIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @BeforeEach
    void setUp() {
        creditCardRepository.deleteAllInBatch();

        creditCardRepository.save( new CreditCard(null, "Siva", "1111222233334444", "123", 2, 2030));
        creditCardRepository.save( new CreditCard(null, "John", "1234123412341234", "123", 3, 2030));

        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldAuthorizePaymentSuccessfully() {
        given()
                .contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "cardNumber": "1111222233334444",
                            "cvv": "123",
                            "expiryMonth": 2,
                            "expiryYear": 2030
                        }
                        """
                )
                .when()
                .post("/api/payments/authorize")
                .then()
                .statusCode(200)
                .body("status", is("ACCEPTED"));
    }

    @Test
    void shouldRejectPaymentWhenCVVIsIncorrect() {
        given()
                .contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "cardNumber": "1111222233334444",
                            "cvv": "111",
                            "expiryMonth": 2,
                            "expiryYear": 2024
                        }
                        """
                )
                .when()
                .post("/api/payments/authorize")
                .then()
                .statusCode(200)
                .body("status", is("REJECTED"));
    }

    @Test
    void shouldFailWhenMandatoryDataIsMissing() {
        given()
                .contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "cardNumber": "1111222233334444",
                            "cvv": "111"
                        }
                        """
                )
                .when()
                .post("/api/payments/authorize")
                .then()
                .statusCode(400);
    }
}