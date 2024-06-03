package com.sivalabs.bookstore.catalog;

import java.math.BigDecimal;

public record Commands() {

    public record CreateProductCommand(
            String code, String name, String description, String imageUrl, BigDecimal price) {}
}
