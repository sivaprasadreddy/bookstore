package com.sivalabs.bookstore.orders.core.models;

import com.sivalabs.bookstore.orders.core.OrderStatus;

public record CreateOrderResponse(String orderId, OrderStatus status) {}
