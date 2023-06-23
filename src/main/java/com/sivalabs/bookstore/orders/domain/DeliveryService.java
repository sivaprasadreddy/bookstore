package com.sivalabs.bookstore.orders.domain;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.common.KafkaHelper;
import com.sivalabs.bookstore.common.model.OrderCancelledEvent;
import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.common.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "GERMANY", "UK");

    private final OrderRepository orderRepository;
    private final KafkaHelper kafkaHelper;
    private final ApplicationProperties properties;

    public DeliveryService(OrderRepository orderRepository, KafkaHelper kafkaHelper, ApplicationProperties properties) {
        this.orderRepository = orderRepository;
        this.kafkaHelper = kafkaHelper;
        this.properties = properties;
    }

    public void process(OrderCreatedEvent event) {
        try {
            if (canBeDelivered(event)) {
                this.updateOrderStatus(event.orderId(), OrderStatus.DELIVERED);
                kafkaHelper.send(properties.deliveredOrdersTopic(), buildOrderDeliveredEvent(event));
            } else {
                this.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED);
                kafkaHelper.send(
                        properties.cancelledOrdersTopic(),
                        buildOrderCancelledEvent(event, "Can't deliver to the location"));
            }
        } catch (RuntimeException e) {
            this.updateOrderStatus(event.orderId(), OrderStatus.ERROR);
            kafkaHelper.send(properties.cancelledOrdersTopic(), buildOrderErrorEvent(event, e.getMessage()));
        }
    }

    private void updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }

    private boolean canBeDelivered(OrderCreatedEvent order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(
                order.deliveryAddress().country().toUpperCase());
    }

    private OrderDeliveredEvent buildOrderDeliveredEvent(OrderCreatedEvent order) {
        return new OrderDeliveredEvent(order.orderId(), order.items(), order.customer(), order.deliveryAddress());
    }

    private OrderCancelledEvent buildOrderCancelledEvent(OrderCreatedEvent order, String reason) {
        return new OrderCancelledEvent(
                order.orderId(), reason, order.items(), order.customer(), order.deliveryAddress());
    }

    private OrderErrorEvent buildOrderErrorEvent(OrderCreatedEvent order, String reason) {
        return new OrderErrorEvent(order.orderId(), reason, order.items(), order.customer(), order.deliveryAddress());
    }
}
