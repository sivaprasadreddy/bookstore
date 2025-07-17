package com.sivalabs.bookstore.catalog.core.models;

import java.math.BigDecimal;

public record ProductDto(Long id, String code, String name, String description, String imageUrl, BigDecimal price) {}
