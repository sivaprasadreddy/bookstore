package com.sivalabs.bookstore.orders.domain;

import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.OrderCancelledEvent;
import com.sivalabs.bookstore.orders.domain.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.domain.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.domain.model.OrderErrorEvent;
import com.sivalabs.bookstore.orders.events.OrderEventPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "GERMANY", "UK");
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public void process(OrderCreatedEvent event) {
        try {
            if (canBeDelivered(event)) {
                this.updateOrderStatus(event.orderId(), OrderStatus.DELIVERED);
                orderEventPublisher.send(buildOrderDeliveredEvent(event));
            } else {
                this.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED);
                orderEventPublisher.send(buildOrderCancelledEvent(event, "Can't deliver to the location"));
            }
        } catch (RuntimeException e) {
            this.updateOrderStatus(event.orderId(), OrderStatus.ERROR);
            orderEventPublisher.send(buildOrderErrorEvent(event, e.getMessage()));
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
