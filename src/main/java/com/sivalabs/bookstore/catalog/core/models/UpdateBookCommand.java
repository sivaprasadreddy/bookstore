package com.sivalabs.bookstore.catalog.core.models;

import java.math.BigDecimal;

public record UpdateBookCommand(
        Long id, String isbn, String name, String description, String imageUrl, BigDecimal price) {}
