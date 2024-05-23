package com.sivalabs.bookstore.notifications.events;

import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedNotificationEventHandler {
    private final NotificationService notificationService;

    @EventListener
    public void handle(OrderCreatedEvent event) {
        try {
            log.info("Received a OrderCreatedEvent with orderId:{}: ", event.orderId());
            notificationService.sendConfirmationNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderCreatedEvent. Error: {}", e.getMessage());
            log.error(e.getMessage(), e);
        }
    }
}
