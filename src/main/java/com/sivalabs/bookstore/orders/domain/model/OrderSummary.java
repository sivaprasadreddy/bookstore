package com.sivalabs.bookstore.orders.domain.model;

import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;

public record OrderSummary(Long id, String orderId, OrderStatus status) {}
