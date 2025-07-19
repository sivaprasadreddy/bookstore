package com.sivalabs.bookstore.orders;

import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.models.OrderStats;
import org.springframework.stereotype.Component;

@Component
public class OrdersAPI {

    private final OrderService orderService;

    public OrdersAPI(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderStats getOrderStats() {
        return orderService.getOrderStats();
    }
}
