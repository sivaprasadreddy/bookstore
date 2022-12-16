package com.sivalabs.bookstore.payment.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

class PaymentControllerTest extends AbstractIntegrationTest {

    @Test
    void shouldAuthorizePaymentSuccessfully() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "cardNumber": "1111222233334444",
                            "cvv": "123",
                            "expiryMonth": 2,
                            "expiryYear": 2030
                        }
                        """)
                .when()
                .post("/api/payments/validate")
                .then()
                .statusCode(200)
                .body("status", is("ACCEPTED"));
    }

    @Test
    void shouldRejectPaymentWhenCVVIsIncorrect() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "cardNumber": "1111222233334444",
                            "cvv": "111",
                            "expiryMonth": 2,
                            "expiryYear": 2024
                        }
                        """)
                .when()
                .post("/api/payments/validate")
                .then()
                .statusCode(200)
                .body("status", is("REJECTED"));
    }

    @Test
    void shouldFailWhenMandatoryDataIsMissing() {
        given().contentType(ContentType.JSON)
                .body(
                        """
                        {
                            "cardNumber": "1111222233334444",
                            "cvv": "111"
                        }
                        """)
                .when()
                .post("/api/payments/validate")
                .then()
                .statusCode(400);
    }
}
