package com.sivalabs.bookstore.orders.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.OrderStatus;
import java.math.BigDecimal;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDto(
        Long id,
        String orderId,
        Long userId,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        OrderStatus status,
        String comments) {

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0.0");
        for (OrderItem orderItem : items) {
            amount = amount.add(orderItem.getSubTotal());
        }
        return amount;
    }
}
