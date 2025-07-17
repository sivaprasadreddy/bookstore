package com.sivalabs.bookstore.orders.core.models;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import java.util.Set;

public record OrderDeliveredEvent(String orderId, Set<OrderItem> items, Customer customer, Address deliveryAddress) {}
