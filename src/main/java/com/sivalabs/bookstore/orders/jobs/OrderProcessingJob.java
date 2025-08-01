package com.sivalabs.bookstore.orders.jobs;

import com.sivalabs.bookstore.orders.core.OrderService;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class OrderProcessingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessingJob.class);
    private final OrderService orderService;

    OrderProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${app.new-orders-job-cron}")
    public void processNewOrders() {
        log.info("Processing new orders at {}", Instant.now());
        orderService.processNewOrders();
        log.info("Finished processing new orders at {}", Instant.now());
    }
}
