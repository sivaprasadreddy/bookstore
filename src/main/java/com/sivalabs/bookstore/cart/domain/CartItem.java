package com.sivalabs.bookstore.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String productCode;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private int quantity;

    public BigDecimal getSubTotal() {
        return productPrice.multiply(new BigDecimal(quantity));
    }
}
