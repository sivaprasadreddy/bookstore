package com.sivalabs.bookstore.orders.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderErrorEventHandler {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.error-orders-topic}", groupId = "orders")
    public void handle(String payload) {
        try {
            OrderErrorEvent event = objectMapper.readValue(payload, OrderErrorEvent.class);
            log.info("Received a OrderErrorEvent with orderId:{}", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderErrorEvent with orderId:{}", event.orderId());
                return;
            }
            orderService.updateOrderStatus(event.orderId(), OrderStatus.ERROR, event.reason());
        } catch (JsonProcessingException e) {
            log.error("Error processing OrderErrorEvent. Payload: {}", payload);
            log.error(e.getMessage(), e);
        }
    }
}
