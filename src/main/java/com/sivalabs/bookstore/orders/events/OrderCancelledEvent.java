package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import java.util.Set;

public record OrderCancelledEvent(
        String orderNumber, String reason, Set<OrderItemDto> items, Customer customer, Address deliveryAddress) {}
