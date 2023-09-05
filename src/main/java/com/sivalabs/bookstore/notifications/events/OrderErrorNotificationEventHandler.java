package com.sivalabs.bookstore.notifications.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderErrorNotificationEventHandler {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.error-orders-topic}", groupId = "notifications")
    public void handle(String payload) {
        try {
            OrderErrorEvent event = objectMapper.readValue(payload, OrderErrorEvent.class);
            log.info("Received a OrderErrorEvent with orderId:{}: ", event.orderId());
            notificationService.sendErrorNotification(event);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error processing OrderErrorEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
