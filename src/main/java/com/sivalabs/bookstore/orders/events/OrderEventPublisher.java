package com.sivalabs.bookstore.orders.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.common.model.OrderCancelledEvent;
import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.common.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationProperties properties;

    public void send(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersTopic(), event);
    }

    public void send(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersTopic(), event);
    }

    public void send(OrderErrorEvent event) {
        this.send(properties.errorOrdersTopic(), event);
    }

    public void send(OrderCreatedEvent event) {
        this.send(properties.newOrdersTopic(), event);
    }

    private void send(String topic, Object payload) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
