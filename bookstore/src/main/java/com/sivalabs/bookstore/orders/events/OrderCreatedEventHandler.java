package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.domain.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderCreatedEventHandler.class);

    private final DeliveryService deliveryService;

    public OrderCreatedEventHandler(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @EventListener
    public void handle(OrderCreatedEvent event) {
        try {
            log.info("Received a OrderCreatedEvent with orderId:{}: ", event.orderId());
            deliveryService.process(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderCreatedEvent. event: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
