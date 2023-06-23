package com.sivalabs.bookstore.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_cards")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false)
    private int expiryMonth;

    @Column(nullable = false)
    private int expiryYear;

    public CreditCard(Long id, String customerName, String cardNumber, String cvv, int expiryMonth, int expiryYear) {
        this.id = id;
        this.customerName = customerName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    public CreditCard() {}

    public Long getId() {
        return this.id;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getCvv() {
        return this.cvv;
    }

    public int getExpiryMonth() {
        return this.expiryMonth;
    }

    public int getExpiryYear() {
        return this.expiryYear;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }
}
