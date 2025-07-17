package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.orders.core.models.OrderCancelledEvent;
import com.sivalabs.bookstore.orders.core.models.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.core.models.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.core.models.OrderErrorEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public OrderEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void send(OrderDeliveredEvent event) {
        this.publish(event);
    }

    public void send(OrderCancelledEvent event) {
        this.publish(event);
    }

    public void send(OrderErrorEvent event) {
        this.publish(event);
    }

    public void send(OrderCreatedEvent event) {
        this.publish(event);
    }

    private void publish(Object payload) {
        eventPublisher.publishEvent(payload);
    }
}
