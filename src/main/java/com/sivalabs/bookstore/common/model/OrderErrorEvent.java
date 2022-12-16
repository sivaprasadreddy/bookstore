package com.sivalabs.bookstore.common.model;

import java.util.Set;

public record OrderErrorEvent(
        String orderId,
        String reason,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress) {}
