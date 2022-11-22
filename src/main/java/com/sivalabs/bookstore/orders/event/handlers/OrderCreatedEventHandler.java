package com.sivalabs.bookstore.orders.event.handlers;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.events.OrderCancelledEvent;
import com.sivalabs.bookstore.events.OrderCreatedEvent;
import com.sivalabs.bookstore.events.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler {
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "GERMANY", "UK");

    private final OrderService orderService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ApplicationProperties properties;

    @KafkaListener(topics = "${app.new-orders-topic}", groupId = "bookstore")
    public void handle(OrderCreatedEvent event) {
        log.info("Received a OrderCreatedEvent with orderId:{}: ", event.getOrderId());
        Order order = orderService.findOrderByOrderId(event.getOrderId()).orElse(null);
        if(order == null) {
            log.info("Received a OrderCreatedEvent with invalid orderId:{}: ", event.getOrderId());
            return;
        }
        if(order.getStatus() != OrderStatus.NEW) {
            log.info("Received a OrderCreatedEvent with invalid status:{} for orderId:{}: ", order.getStatus(), event.getOrderId());
            return;
        }
        // logic to process order delivery
        if(canBeDelivered(order)) {
            orderService.updateOrderStatus(order.getOrderId(), OrderStatus.DELIVERED, null);
            kafkaTemplate.send(properties.deliveredOrdersTopic(), new OrderDeliveredEvent(order.getOrderId()));
        } else {
            orderService.updateOrderStatus(order.getOrderId(), OrderStatus.CANCELLED, "Can't deliver to your delivery address");
            kafkaTemplate.send(properties.cancelledOrdersTopic(), new OrderCancelledEvent(order.getOrderId()));
        }
    }

    private boolean canBeDelivered(Order order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(order.getDeliveryAddressCountry().toUpperCase());
    }

}
