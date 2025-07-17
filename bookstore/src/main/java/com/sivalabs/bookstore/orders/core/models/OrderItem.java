package com.sivalabs.bookstore.orders.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderItem(
        @NotBlank(message = "Code is required") String code,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Price is required") @DecimalMin(value = "0.1", message = "Price should be greater than 0") BigDecimal price,
        @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity should be greater than 0") Integer quantity) {
    public BigDecimal getSubTotal() {
        return price.multiply(new BigDecimal(quantity));
    }
}
