package com.sivalabs.bookstore.orders.core.models;

public record ProcessPaymentCmd(String cardNumber, int expiryMonth, int expiryYear, String cvv) {}
