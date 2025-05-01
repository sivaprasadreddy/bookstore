package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.orders.domain.NotificationService;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class OrderErrorEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderErrorEventHandler.class);
    private final OrderService orderService;
    private final NotificationService notificationService;

    OrderErrorEventHandler(OrderService orderService, NotificationService notificationService) {
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @EventListener
    public void handle(OrderErrorEvent event) {
        try {
            log.info("Received a OrderErrorEvent with orderId:{}", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderErrorEvent with orderId:{}", event.orderId());
                return;
            }
            orderService.updateOrderStatus(event.orderId(), OrderStatus.ERROR, event.reason());
            notificationService.sendErrorNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderErrorEvent. Payload: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
