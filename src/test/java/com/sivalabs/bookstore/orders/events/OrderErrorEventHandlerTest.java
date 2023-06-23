package com.sivalabs.bookstore.orders.events;

import static com.sivalabs.bookstore.orders.domain.entity.OrderStatus.ERROR;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.KafkaHelper;
import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import com.sivalabs.bookstore.orders.domain.OrderRepository;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

class OrderErrorEventHandlerTest extends AbstractIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(OrderErrorEventHandlerTest.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaHelper kafkaHelper;

    @Autowired
    private ApplicationProperties properties;

    @Test
    void shouldHandleOrderErrorEvent() {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.NEW);
        order.setCustomerName("Siva");
        order.setCustomerEmail("siva@gmail.com");
        order.setCustomerPhone("9999999999");
        order.setDeliveryAddressLine1("addr line 1");
        order.setDeliveryAddressLine2("addr line 2");
        order.setDeliveryAddressCity("Hyderabad");
        order.setDeliveryAddressState("Telangana");
        order.setDeliveryAddressZipCode("500072");
        order.setDeliveryAddressCountry("India");

        orderRepository.saveAndFlush(order);

        kafkaHelper.send(
                properties.errorOrdersTopic(),
                new OrderErrorEvent(
                        order.getOrderId(),
                        "testing",
                        Set.of(),
                        new Customer("Siva", "siva@gmail.com", "9999999999"),
                        new Address("addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India")));

        await().atMost(30, SECONDS).untilAsserted(() -> {
            Optional<Order> orderOptional = orderRepository.findByOrderId(order.getOrderId());
            assertThat(orderOptional).isPresent();
            assertThat(orderOptional.get().getStatus()).isEqualTo(ERROR);
        });
    }
}
