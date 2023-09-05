package com.sivalabs.bookstore.notifications.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.common.model.OrderCancelledEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCancelledNotificationEventHandler {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.cancelled-orders-topic}", groupId = "notifications")
    public void handle(String payload) {
        try {
            OrderCancelledEvent event = objectMapper.readValue(payload, OrderCancelledEvent.class);
            log.info("Received a OrderCancelledEvent with orderId:{}: ", event.orderId());
            notificationService.sendCancelledNotification(event);
        } catch (JsonProcessingException e) {
            log.error("Error processing OrderCancelledEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
