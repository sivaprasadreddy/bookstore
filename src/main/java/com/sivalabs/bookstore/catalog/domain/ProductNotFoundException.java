package com.sivalabs.bookstore.catalog.domain;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String code) {
        super("Product with code: " + code + " not found");
    }
}
