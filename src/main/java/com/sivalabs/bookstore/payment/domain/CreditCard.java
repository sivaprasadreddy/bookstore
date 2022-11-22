package com.sivalabs.bookstore.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "creditcards")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
