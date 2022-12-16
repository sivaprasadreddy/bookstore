package com.sivalabs.bookstore.payment.domain;

public record PaymentResponse(PaymentStatus status) {
    public enum PaymentStatus {
        ACCEPTED,
        REJECTED
    }
}
