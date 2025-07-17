package com.sivalabs.bookstore.orders.core.models;

import java.util.Set;

public record OrderCreatedEvent(String orderId, Set<OrderItem> items, Customer customer, Address deliveryAddress) {}
