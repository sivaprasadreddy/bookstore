package com.sivalabs.bookstore.orders.domain.model;

import java.util.Set;

public record OrderCreatedEvent(String orderId, Set<OrderItem> items, Customer customer, Address deliveryAddress) {}
