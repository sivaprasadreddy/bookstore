package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.orders.core.DeliveryService;
import com.sivalabs.bookstore.orders.core.NotificationService;
import com.sivalabs.bookstore.orders.core.models.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class OrderCreatedEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderCreatedEventHandler.class);
    private final DeliveryService deliveryService;
    private final NotificationService notificationService;

    OrderCreatedEventHandler(DeliveryService deliveryService, NotificationService notificationService) {
        this.deliveryService = deliveryService;
        this.notificationService = notificationService;
    }

    @EventListener
    public void handle(OrderCreatedEvent event) {
        try {
            log.info("Received a OrderCreatedEvent with orderNumber:{}: ", event.orderNumber());
            deliveryService.process(event);
            notificationService.sendConfirmationNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderCreatedEvent. event: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
