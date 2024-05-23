package com.sivalabs.bookstore.notifications.events;

import com.sivalabs.bookstore.common.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDeliveredNotificationEventHandler {
    private final NotificationService notificationService;

    @EventListener
    public void handle(OrderDeliveredEvent event) {
        try {
            log.info("Received a OrderDeliveredEvent with orderId:{}: ", event.orderId());
            notificationService.sendDeliveredNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderDeliveredEvent. Error: {}", e.getMessage());
            log.error(e.getMessage(), e);
        }
    }
}
