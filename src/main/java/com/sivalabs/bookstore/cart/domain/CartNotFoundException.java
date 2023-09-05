package com.sivalabs.bookstore.cart.domain;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String cartId) {
        super("Cart with cartId: " + cartId + " not found");
    }
}
