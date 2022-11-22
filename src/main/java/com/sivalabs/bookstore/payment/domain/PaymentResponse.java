package com.sivalabs.bookstore.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private PaymentStatus status;

    public enum PaymentStatus {
        ACCEPTED, REJECTED
    }
}
