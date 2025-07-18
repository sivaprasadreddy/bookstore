package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.orders.core.NotificationService;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.OrderStatus;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class OrderCancelledEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderCancelledEventHandler.class);
    private final OrderService orderService;
    private final NotificationService notificationService;

    OrderCancelledEventHandler(OrderService orderService, NotificationService notificationService) {
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @ApplicationModuleListener
    public void handle(OrderCancelledEvent event) {
        try {
            log.info("Received a OrderCancelledEvent with orderNumber:{}: ", event.orderNumber());
            OrderDto order =
                    orderService.findOrderByOrderNumber(event.orderNumber()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderCancelledEvent with orderNumber:{}: ", event.orderNumber());
                return;
            }
            orderService.updateOrderStatus(event.orderNumber(), OrderStatus.CANCELLED, event.reason());
            notificationService.sendCancelledNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderCancelledEvent. event: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
