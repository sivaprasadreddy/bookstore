package com.sivalabs.bookstore.orders.event.handlers;

import com.sivalabs.bookstore.events.OrderCancelledEvent;
import com.sivalabs.bookstore.events.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCancelledEventHandler {
    private final OrderService orderService;

    @KafkaListener(topics = "${app.cancelled-orders-topic}", groupId = "bookstore")
    public void handle(OrderCancelledEvent event) {
        log.info("Received a OrderCancelledEvent with orderId:{}: ", event.getOrderId());
        Order order = orderService.findOrderByOrderId(event.getOrderId()).orElse(null);
        if(order == null) {
            log.info("Received invalid OrderCancelledEvent with orderId:{}: ", event.getOrderId());
            return;
        }
        String email = """
                Hi %s,
                This email is to notify you that your order : %s is cancelled.
                
                Thanks,
                BookStore Team
                """.formatted(order.getCustomerName(), order.getOrderId());
        log.info("==========================================================");
        log.info("                    Order Cancellation Notification       ");
        log.info("==========================================================");
        log.info(email);
        log.info("==========================================================");
    }
}
