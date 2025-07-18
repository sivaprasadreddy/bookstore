package com.sivalabs.bookstore.orders.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    OrderEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void send(OrderDeliveredEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void send(OrderCancelledEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void send(OrderErrorEvent event) {
        eventPublisher.publishEvent(event);
    }

    public void send(OrderCreatedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
