package com.sivalabs.bookstore.orders.jobs;

import com.sivalabs.bookstore.orders.domain.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingJob {
    private final OrderService orderService;

    @Scheduled(cron = "${app.new-orders-job-cron}")
    public void processNewOrders() {
        orderService.processNewOrders();
    }

    @Scheduled(cron = "${app.rejected-orders-job-cron}")
    public void processPaymentRejectedOrders() {
        orderService.processPaymentRejectedOrders();
    }
}
