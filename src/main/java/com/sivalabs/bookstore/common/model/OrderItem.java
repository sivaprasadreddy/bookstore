package com.sivalabs.bookstore.common.model;

import java.math.BigDecimal;

public record OrderItem(String isbn, String title, BigDecimal price, Integer quantity) {}
