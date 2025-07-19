package com.sivalabs.bookstore.orders;

import com.sivalabs.bookstore.common.model.PagedResult;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderStats;
import com.sivalabs.bookstore.orders.core.models.OrderStatus;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import java.util.Optional;
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

    public PagedResult<OrderSummary> findAllOrders(int pageNo, int pageSize) {
        return orderService.findAllOrders(pageNo, pageSize);
    }

    public Optional<OrderDto> findOrderByOrderNumber(String orderNumber) {
        return orderService.findOrderByOrderNumber(orderNumber);
    }

    public void updateOrderStatus(String orderNumber, OrderStatus status, String comments) {
        orderService.updateOrderStatus(orderNumber, status, comments);
    }
}
