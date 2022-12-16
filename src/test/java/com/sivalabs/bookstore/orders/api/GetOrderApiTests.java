package com.sivalabs.bookstore.orders.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.OrderConfirmationDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderItemDTO;
import com.sivalabs.bookstore.orders.domain.model.Payment;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GetOrderApiTests extends AbstractIntegrationTest {

    @Autowired private OrderService orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        Customer customer = new Customer("Siva", "siva@gmail.com", "99999999999");
        createOrderRequest.setCustomer(customer);

        Address address =
                new Address(
                        "addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India");
        createOrderRequest.setDeliveryAddress(address);

        Payment payment = new Payment();
        payment.setCardNumber("1234123412341234");
        payment.setCvv("123");
        payment.setExpiryMonth(10);
        payment.setExpiryYear(2025);
        createOrderRequest.setPayment(payment);

        createOrderRequest.setItems(
                Set.of(new OrderItemDTO("P100", "Product 1", BigDecimal.TEN, 1)));
        OrderConfirmationDTO orderConfirmationDTO = orderService.createOrder(createOrderRequest);

        OrderDTO orderDTO =
                given().when()
                        .get("/api/orders/{orderId}", orderConfirmationDTO.getOrderId())
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(OrderDTO.class);

        assertThat(orderDTO.getOrderId()).isEqualTo(orderConfirmationDTO.getOrderId());
        assertThat(orderDTO.getItems()).hasSize(1);
    }

    @Test
    void shouldReturnNotFoundWhenOrderIdNotExist() {
        given().when().get("/api/orders/{orderId}", "non-existing-order-id").then().statusCode(404);
    }
}
