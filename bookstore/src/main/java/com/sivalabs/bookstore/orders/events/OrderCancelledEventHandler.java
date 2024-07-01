package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.orders.domain.NotificationService;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.OrderCancelledEvent;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderCancelledEventHandler {
    private final OrderService orderService;
    private final NotificationService notificationService;

    @EventListener
    public void handle(OrderCancelledEvent event) {
        try {
            log.info("Received a OrderCancelledEvent with orderId:{}: ", event.orderId());
            OrderDTO order = orderService.findOrderByOrderId(event.orderId()).orElse(null);
            if (order == null) {
                log.info("Received invalid OrderCancelledEvent with orderId:{}: ", event.orderId());
                return;
            }
            orderService.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED, event.reason());
            notificationService.sendCancelledNotification(event);
        } catch (RuntimeException e) {
            log.error("Error processing OrderCancelledEvent. event: {}", event);
            log.error(e.getMessage(), e);
        }
    }
}
