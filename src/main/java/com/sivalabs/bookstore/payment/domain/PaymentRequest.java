package com.sivalabs.bookstore.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String cardNumber;
    private String cvv;
    private int expiryMonth;
    private int expiryYear;
}
