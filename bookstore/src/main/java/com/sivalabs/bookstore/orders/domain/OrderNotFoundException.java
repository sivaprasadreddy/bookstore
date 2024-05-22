package com.sivalabs.bookstore.orders.domain;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("Order with orderId " + orderId + " not found");
    }
}
