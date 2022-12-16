package com.sivalabs.bookstore.orders.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.domain.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderCreatedEventHandler.class);

    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;

    public OrderCreatedEventHandler(DeliveryService deliveryService, ObjectMapper objectMapper) {
        this.deliveryService = deliveryService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.new-orders-topic}", groupId = "orders")
    public void handle(String payload) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
            log.info("Received a OrderCreatedEvent with orderId:{}: ", event.orderId());
            deliveryService.process(event);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("Error processing OrderCreatedEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
