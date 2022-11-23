package com.sivalabs.bookstore.orders.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
public class CreateOrderRequest {

    @NotEmpty(message = "Items cannot be empty.")
    private Set<LineItem> items;

    @NotBlank(message = "Customer Name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email
    private String customerEmail;

    @NotBlank(message = "DeliveryAddress Line1 is required")
    private String deliveryAddressLine1;

    private String deliveryAddressLine2;

    @NotBlank(message = "DeliveryAddress City is required")
    private String deliveryAddressCity;

    @NotBlank(message = "DeliveryAddress State is required")
    private String deliveryAddressState;

    @NotBlank(message = "DeliveryAddress ZipCode is required")
    private String deliveryAddressZipCode;

    @NotBlank(message = "DeliveryAddress Country is required")
    private String deliveryAddressCountry;

    @NotBlank(message = "Card Number is required")
    private String cardNumber;

    @NotBlank(message = "CVV is required")
    private String cvv;

    @NotNull
    private Integer expiryMonth;

    @NotNull
    private Integer expiryYear;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineItem {
        @NotBlank(message = "productCode is required")
        private String productCode;
        private String productName;
        private BigDecimal productPrice;
        @NotNull
        @Min(1)
        private Integer quantity;
    }
}
