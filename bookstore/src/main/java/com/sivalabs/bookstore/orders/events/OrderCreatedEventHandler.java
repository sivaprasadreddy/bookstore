package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.orders.domain.DeliveryService;
import com.sivalabs.bookstore.orders.domain.NotificationService;
import com.sivalabs.bookstore.orders.domain.model.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler {
    private final DeliveryService deliveryService;
    private final NotificationService notificationService;

    @EventListener
    public void handle(OrderCreatedEvent event) {
        try {
            log.info("Received a OrderCreatedEvent with orderId:{}: ", event.orderId());
            deliveryService.process(event);
            notificationService.sendConfirmationNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderCreatedEvent. event: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
