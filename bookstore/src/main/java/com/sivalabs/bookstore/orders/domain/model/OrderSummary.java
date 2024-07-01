package com.sivalabs.bookstore.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sivalabs.bookstore.orders.domain.OrderStatus;
import java.time.LocalDateTime;

public record OrderSummary(
        Long id,
        String orderId,
        OrderStatus status,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt) {}
