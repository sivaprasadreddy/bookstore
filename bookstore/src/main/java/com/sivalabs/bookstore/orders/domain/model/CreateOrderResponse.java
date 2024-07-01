package com.sivalabs.bookstore.orders.domain.model;

import com.sivalabs.bookstore.orders.domain.OrderStatus;

public record CreateOrderResponse(String orderId, OrderStatus status) {}
