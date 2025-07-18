package com.sivalabs.bookstore.orders.core;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderNumber) {
        super("Order with orderNumber " + orderNumber + " not found");
    }
}
