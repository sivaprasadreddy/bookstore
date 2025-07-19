package com.sivalabs.bookstore.orders.core.models;

import java.time.LocalDateTime;

public record OrderSummary(Long id, String orderNumber, OrderStatus status, LocalDateTime createdAt) {}
