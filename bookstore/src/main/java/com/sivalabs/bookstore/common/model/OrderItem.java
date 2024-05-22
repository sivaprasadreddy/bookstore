package com.sivalabs.bookstore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderItem(String code, String name, BigDecimal price, Integer quantity) {
    public BigDecimal getSubTotal() {
        return price.multiply(new BigDecimal(quantity));
    }
}
