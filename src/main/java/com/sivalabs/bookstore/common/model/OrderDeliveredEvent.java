package com.sivalabs.bookstore.common.model;

import java.util.Set;

public record OrderDeliveredEvent(String orderId, Set<OrderItem> items, Customer customer, Address deliveryAddress) {}
