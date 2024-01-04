package com.sivalabs.bookstore.orders.jobs;

import com.sivalabs.bookstore.orders.domain.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProcessingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingJob.class);
    private final OrderService orderService;

    @Scheduled(cron = "${app.new-orders-job-cron}")
    public void processNewOrders() {
        log.info("Processing new orders job started");
        orderService.processNewOrders();
    }

    @Scheduled(cron = "${app.rejected-orders-job-cron}")
    public void processPaymentRejectedOrders() {
        orderService.processPaymentRejectedOrders();
    }
}
