package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.common.model.OrderCancelledEvent;
import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.common.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.common.model.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationProperties properties;

    public void send(OrderDeliveredEvent event) {
        this.send("deliveredOrders", event);
    }

    public void send(OrderCancelledEvent event) {
        this.send("cancelledOrders", event);
    }

    public void send(OrderErrorEvent event) {
        this.send("errorOrders", event);
    }

    public void send(OrderCreatedEvent event) {
        this.send("newOrders", event);
    }

    private void send(String eventType, Object event) {
        log.info("Sending event: {} to topic: {}", event, eventType);
        eventPublisher.publishEvent(event);
    }
}
