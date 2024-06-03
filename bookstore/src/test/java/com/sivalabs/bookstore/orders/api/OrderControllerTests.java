package com.sivalabs.bookstore.orders.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderResponse;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import io.restassured.http.ContentType;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/test-products-data.sql", "/test-orders-data.sql"})
class OrderControllerTests extends AbstractIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Nested
    class CreateOrderApiTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            CreateOrderResponse createOrderResponse = given().contentType(ContentType.JSON)
                    .body(
                            """

                                        {
                                        "customer" : {
                                            "name": "Siva",
                                            "email": "siva@gmail.com",
                                            "phone": "999999999"
                                        },
                                        "deliveryAddress" : {
                                            "addressLine1": "Birkelweg",
                                            "addressLine2": "Hans-Edenhofer-Straße 23",
                                            "city": "Berlin",
                                            "state": "Berlin",
                                            "zipCode": "94258",
                                            "country": "Germany"
                                        },
                                        "items": [
                                            {
                                                "code": "P100",
                                                "name": "The Hunger Games",
                                                "price": 34.0,
                                                "quantity": 1
                                            }
                                        ]
                                    }
                                    """)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(202)
                    .body("orderId", notNullValue())
                    .body("status", is("NEW"))
                    .extract()
                    .body()
                    .as(CreateOrderResponse.class);

            Optional<OrderDTO> orderOptional = orderService.findOrderByOrderId(createOrderResponse.orderId());
            assertThat(orderOptional).isPresent();
        }

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            given().contentType(ContentType.JSON)
                    .body(
                            """
                                    {
                                        "customer" : {
                                            "name": "Siva",
                                            "email": "siva@gmail.com",
                                            "phone": ""
                                        },
                                        "deliveryAddress" : {
                                            "addressLine1": "Birkelweg",
                                            "addressLine2": "Hans-Edenhofer-Straße 23",
                                            "city": "Berlin",
                                            "state": "Berlin",
                                            "zipCode": "94258",
                                            "country": "Germany"
                                        },
                                        "items": [
                                            {
                                                "code": "P100",
                                                "name": "The Hunger Games",
                                                "price": 34.00,
                                                "quantity": 1
                                            }
                                        ]
                                    }
                                    """)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(400);
        }
    }

    @Nested
    class GetOrderApiTests {
        String orderId = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            OrderDTO orderDTO = given().when()
                    .get("/api/orders/{orderId}", orderId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(OrderDTO.class);

            assertThat(orderDTO.orderId()).isEqualTo(orderId);
            assertThat(orderDTO.items()).hasSize(2);
        }

        @Test
        void shouldReturnNotFoundWhenOrderIdNotExist() {
            given().when()
                    .get("/api/orders/{orderId}", "non-existing-order-id")
                    .then()
                    .statusCode(404);
        }
    }
}
