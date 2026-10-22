package com.sivalabs.bookstore.orders.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
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
