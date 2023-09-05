package com.sivalabs.bookstore.notifications.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedNotificationEventHandler {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.new-orders-topic}", groupId = "notifications")
    public void handle(String payload) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
            log.info("Received a OrderCreatedEvent with orderId:{}: ", event.orderId());
            notificationService.sendConfirmationNotification(event);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error processing OrderCreatedEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
