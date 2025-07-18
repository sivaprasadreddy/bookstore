package com.sivalabs.bookstore.catalog.core.models;

import java.math.BigDecimal;

public record BookDto(Long id, String isbn, String name, String description, String imageUrl, BigDecimal price) {
    public String getDisplayName() {
        if (name.length() <= 20) {
            return name;
        }
        return name.substring(0, 20) + "...";
    }
}
