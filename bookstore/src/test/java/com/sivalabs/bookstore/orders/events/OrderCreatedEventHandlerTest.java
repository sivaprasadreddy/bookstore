package com.sivalabs.bookstore.orders.events;

import static com.sivalabs.bookstore.orders.domain.entity.OrderStatus.DELIVERED;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.domain.OrderRepository;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.Address;
import com.sivalabs.bookstore.orders.domain.model.Customer;
import com.sivalabs.bookstore.orders.domain.model.OrderCreatedEvent;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class OrderCreatedEventHandlerTest extends AbstractIntegrationTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Test
    void shouldHandleOrderCreatedEvent() {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.NEW);
        Customer customer = new Customer("Siva", "siva@gmail.com", "9999999999");
        Address deliveryAddress =
                new Address("addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India");
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);

        orderRepository.saveAndFlush(order);

        OrderCreatedEvent event = new OrderCreatedEvent(order.getOrderId(), Set.of(), customer, deliveryAddress);

        log.info("Created OrderId:{}", event.orderId());

        orderEventPublisher.send(event);

        await().atMost(30, SECONDS).untilAsserted(() -> {
            Optional<Order> orderOptional = orderRepository.findByOrderId(event.orderId());
            assertThat(orderOptional).isNotEmpty();
            assertThat(orderOptional.get().getOrderId()).isEqualTo(event.orderId());
            assertThat(orderOptional.get().getStatus()).isEqualTo(DELIVERED);
        });
    }
}
