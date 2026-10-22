package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.orders.core.models.OrderStatus;
import com.sivalabs.bookstore.orders.events.OrderCancelledEvent;
import com.sivalabs.bookstore.orders.events.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.events.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.events.OrderErrorEvent;
import com.sivalabs.bookstore.orders.events.OrderEventPublisher;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryService {
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "GERMANY", "UK");
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    DeliveryService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public void process(OrderCreatedEvent event) {
        try {
            if (canBeDelivered(event)) {
                this.updateOrderStatus(event.orderNumber(), OrderStatus.DELIVERED);
                orderEventPublisher.send(buildOrderDeliveredEvent(event));
            } else {
                this.updateOrderStatus(event.orderNumber(), OrderStatus.CANCELLED);
                orderEventPublisher.send(buildOrderCancelledEvent(event, "Can't deliver to the location"));
            }
        } catch (RuntimeException e) {
            this.updateOrderStatus(event.orderNumber(), OrderStatus.ERROR);
            orderEventPublisher.send(buildOrderErrorEvent(event, e.getMessage()));
        }
    }

    private void updateOrderStatus(String orderNumber, OrderStatus status) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }

    private boolean canBeDelivered(OrderCreatedEvent order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(
                order.deliveryAddress().country().toUpperCase(Locale.getDefault()));
    }

    private OrderDeliveredEvent buildOrderDeliveredEvent(OrderCreatedEvent order) {
        return new OrderDeliveredEvent(order.orderNumber(), order.items(), order.customer(), order.deliveryAddress());
    }

    private OrderCancelledEvent buildOrderCancelledEvent(OrderCreatedEvent order, String reason) {
        return new OrderCancelledEvent(
                order.orderNumber(), reason, order.items(), order.customer(), order.deliveryAddress());
    }

    private OrderErrorEvent buildOrderErrorEvent(OrderCreatedEvent order, String reason) {
        return new OrderErrorEvent(
                order.orderNumber(), reason, order.items(), order.customer(), order.deliveryAddress());
    }
}
