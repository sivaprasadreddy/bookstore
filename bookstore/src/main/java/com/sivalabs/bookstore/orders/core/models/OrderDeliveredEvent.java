package com.sivalabs.bookstore.orders.core.models;

import java.util.Set;

public record OrderDeliveredEvent(String orderId, Set<OrderItem> items, Customer customer, Address deliveryAddress) {}
