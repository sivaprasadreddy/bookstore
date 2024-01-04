package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.common.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.domain.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler {
    private final DeliveryService deliveryService;

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
