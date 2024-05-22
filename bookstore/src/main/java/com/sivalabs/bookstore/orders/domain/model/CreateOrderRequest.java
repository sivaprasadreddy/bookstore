package com.sivalabs.bookstore.orders.domain.model;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.common.model.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

public record CreateOrderRequest(
        @NotEmpty(message = "Items cannot be empty") @Valid Set<OrderItem> items,
        @Valid Customer customer,
        @Valid Address deliveryAddress,
        @Valid Payment payment)
        implements Serializable {

    public record Payment(
            @NotBlank(message = "Card Number is required") String cardNumber,
            @NotBlank(message = "CVV is required") String cvv,
            @NotNull Integer expiryMonth,
            @NotNull Integer expiryYear)
            implements Serializable {}
}
