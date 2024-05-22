package com.sivalabs.bookstore.orders.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.common.model.OrderItem;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderResponse;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetOrderApiTests extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

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

        CreateOrderRequest.Payment payment = new CreateOrderRequest.Payment("1234123412341234", "123", 10, 2025);

        Set<OrderItem> items = Set.of(new OrderItem("P100", "Product 1", new BigDecimal("25.50"), 1));
        return new CreateOrderRequest(items, customer, address, payment);
    }
}
