package com.sivalabs.bookstore.orders.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderItemDTO {
    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;

    public OrderItemDTO(String code, String name, BigDecimal price, Integer quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItemDTO() {}

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
