package com.sivalabs.bookstore.orders.event.handlers;

import com.sivalabs.bookstore.events.OrderDeliveredEvent;
import com.sivalabs.bookstore.notifications.NotificationService;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDeliveredEventHandler {
    private final OrderService orderService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "${app.delivered-orders-topic}", groupId = "bookstore")
    public void handle(OrderDeliveredEvent event) {
        log.info("Received a OrderDeliveredEvent with orderId:{}: ", event.getOrderId());
        Order order = orderService.findOrderByOrderId(event.getOrderId()).orElse(null);
        if(order == null) {
            log.info("Received invalid OrderDeliveredEvent with orderId:{}: ", event.getOrderId());
            return;
        }
        notificationService.sendDeliveredNotification(order);
    }
}
