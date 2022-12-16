package com.sivalabs.bookstore.cart.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record CartItemRequestDTO(@NotEmpty String code, @Min(0) int quantity) {}
