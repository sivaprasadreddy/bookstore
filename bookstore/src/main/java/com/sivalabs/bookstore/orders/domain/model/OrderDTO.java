package com.sivalabs.bookstore.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sivalabs.bookstore.orders.domain.OrderStatus;
import java.math.BigDecimal;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDTO(
        Long id,
        String orderId,
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
