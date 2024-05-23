package com.sivalabs.bookstore.orders.events;

import static com.sivalabs.bookstore.orders.domain.entity.OrderStatus.ERROR;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.domain.OrderRepository;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.Address;
import com.sivalabs.bookstore.orders.domain.model.Customer;
import com.sivalabs.bookstore.orders.domain.model.OrderErrorEvent;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderErrorEventHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Test
    void shouldHandleOrderErrorEvent() {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.NEW);
        Customer customer = new Customer("Siva", "siva@gmail.com", "9999999999");
        Address deliveryAddress =
                new Address("addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India");
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);

        orderRepository.saveAndFlush(order);

        orderEventPublisher.send(
                new OrderErrorEvent(order.getOrderId(), "testing", Set.of(), customer, deliveryAddress));

        await().atMost(30, SECONDS).untilAsserted(() -> {
            Optional<Order> orderOptional = orderRepository.findByOrderId(order.getOrderId());
            assertThat(orderOptional).isPresent();
            assertThat(orderOptional.get().getStatus()).isEqualTo(ERROR);
        });
    }
}
