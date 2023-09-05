package com.sivalabs.bookstore.notifications.events;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import com.sivalabs.bookstore.orders.events.OrderEventPublisher;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class OrderErrorNotificationEventHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Test
    void shouldHandleOrderErrorEvent() {
        Customer customer = new Customer("Siva", "siva@gmail.com", "999999999");
        OrderErrorEvent event =
                new OrderErrorEvent(UUID.randomUUID().toString(), "test error", Set.of(), customer, null);
        log.info("OrderErrorEvent with OrderId:{}", event.orderId());

        orderEventPublisher.send(event);

        ArgumentCaptor<OrderErrorEvent> captor = ArgumentCaptor.forClass(OrderErrorEvent.class);
        await().atMost(30, SECONDS).untilAsserted(() -> {
            verify(notificationService).sendErrorNotification(captor.capture());
            OrderErrorEvent orderErrorEvent = captor.getValue();
            assertThat(orderErrorEvent.orderId()).isEqualTo(event.orderId());
        });
    }
}
