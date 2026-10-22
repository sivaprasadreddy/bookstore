package com.sivalabs.bookstore.orders.core.models;

import java.time.Instant;

public record OrderSummary(Long id, String orderNumber, OrderStatus status, Instant createdAt) {}
