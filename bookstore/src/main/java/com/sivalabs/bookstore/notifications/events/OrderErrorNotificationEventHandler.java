package com.sivalabs.bookstore.notifications.events;

import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderErrorNotificationEventHandler {
    private final NotificationService notificationService;

    @EventListener
    public void handle(OrderErrorEvent event) {
        try {
            log.info("Received a OrderErrorEvent with orderId:{}: ", event.orderId());
            notificationService.sendErrorNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderErrorEvent. Error: {}", e.getMessage());
            log.error(e.getMessage(), e);
        }
    }
}
