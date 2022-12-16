package com.sivalabs.bookstore.orders.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Payment {
    @NotBlank(message = "Card Number is required")
    private String cardNumber;

    @NotBlank(message = "CVV is required")
    private String cvv;

    @NotNull private Integer expiryMonth;

    @NotNull private Integer expiryYear;

    public Payment(String cardNumber, String cvv, Integer expiryMonth, Integer expiryYear) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    public Payment() {}

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getCvv() {
        return this.cvv;
    }

    public Integer getExpiryMonth() {
        return this.expiryMonth;
    }

    public Integer getExpiryYear() {
        return this.expiryYear;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }
}
