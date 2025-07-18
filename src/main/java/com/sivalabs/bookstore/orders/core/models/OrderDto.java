package com.sivalabs.bookstore.orders.core.models;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.OrderStatus;
import java.math.BigDecimal;
import java.util.Set;

public record OrderDto(
        Long id,
        String orderNumber,
        Long userId,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        OrderStatus status,
        String comments) {

    public BigDecimal getTotalAmount() {
        return items.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
