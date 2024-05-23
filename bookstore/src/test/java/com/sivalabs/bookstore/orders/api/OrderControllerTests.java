package com.sivalabs.bookstore.orders.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.model.Address;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderResponse;
import com.sivalabs.bookstore.orders.domain.model.Customer;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderItem;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
                                                "name": "Product 1",
                                                "price": 25.50,
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
                                                "name": "Product 1",
                                                "price": 25.50,
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
        @Test
        void shouldGetOrderSuccessfully() {
            CreateOrderRequest createOrderRequest = getCreateOrderRequest();
            CreateOrderResponse createOrderResponse = orderService.createOrder(createOrderRequest);

            OrderDTO orderDTO = given().when()
                    .get("/api/orders/{orderId}", createOrderResponse.orderId())
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(OrderDTO.class);

            assertThat(orderDTO.orderId()).isEqualTo(createOrderResponse.orderId());
            assertThat(orderDTO.items()).hasSize(1);
        }

        @Test
        void shouldReturnNotFoundWhenOrderIdNotExist() {
            given().when()
                    .get("/api/orders/{orderId}", "non-existing-order-id")
                    .then()
                    .statusCode(404);
        }

        private static CreateOrderRequest getCreateOrderRequest() {
            Customer customer = new Customer("Siva", "siva@gmail.com", "99999999999");

            Address address = new Address("addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India");

            Set<OrderItem> items = Set.of(new OrderItem("P100", "Product 1", new BigDecimal("25.50"), 1));
            return new CreateOrderRequest(items, customer, address);
        }
    }
}
