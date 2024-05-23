package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.common.model.OrderCancelledEvent;
import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.common.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

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
