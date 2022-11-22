package com.sivalabs.bookstore.cart.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemRequestDTO {
    private String productCode;
    private int quantity;
}
