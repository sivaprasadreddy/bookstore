package com.sivalabs.bookstore.orders.core;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("Order with orderId " + orderId + " not found");
    }
}
