package com.sivalabs.bookstore.catalog.core;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String msg) {
        super(msg);
    }

    public static ProductNotFoundException withCode(String code) {
        return new ProductNotFoundException("Product with code: " + code + " not found");
    }
}
