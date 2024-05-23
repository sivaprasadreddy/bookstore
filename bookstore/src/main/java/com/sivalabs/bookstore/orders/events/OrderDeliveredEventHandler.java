package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.common.model.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveredEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderDeliveredEventHandler.class);

    private final OrderService orderService;

    public OrderDeliveredEventHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @EventListener
    public void handle(OrderDeliveredEvent event) {
        try {
            log.info("Received a OrderDeliveredEvent with orderId:{}: ", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderDeliveredEvent with orderId:{}: ", event.orderId());
                return;
            }
            orderService.updateOrderStatus(order.orderId(), OrderStatus.DELIVERED, null);
        } catch (RuntimeException e) {
            log.error("Error processing OrderDeliveredEvent. event: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
