package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.orders.core.NotificationService;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.OrderStatus;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class OrderDeliveredEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderDeliveredEventHandler.class);
    private final OrderService orderService;
    private final NotificationService notificationService;

    OrderDeliveredEventHandler(OrderService orderService, NotificationService notificationService) {
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @EventListener
    public void handle(OrderDeliveredEvent event) {
        try {
            log.info("Received a OrderDeliveredEvent with orderNumber:{}: ", event.orderNumber());
            OrderDto order =
                    orderService.findOrderByOrderNumber(event.orderNumber()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderDeliveredEvent with orderNumber:{}: ", event.orderNumber());
                return;
            }
            orderService.updateOrderStatus(order.orderNumber(), OrderStatus.DELIVERED, null);
            notificationService.sendDeliveredNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderDeliveredEvent. event: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
