package com.sivalabs.bookstore.orders.domain.model;

import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;

public record CreateOrderResponse(String orderId, OrderStatus status) {}
