package com.sivalabs.bookstore.notifications.events;

import com.sivalabs.bookstore.common.model.OrderCancelledEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCancelledNotificationEventHandler {
    private final NotificationService notificationService;

    @EventListener
    public void handle(OrderCancelledEvent event) {
        try {
            log.info("Received a OrderCancelledEvent with orderId:{}: ", event.orderId());
            notificationService.sendCancelledNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderCancelledEvent. Error: {}", e.getMessage());
            log.error(e.getMessage(), e);
        }
    }
}
