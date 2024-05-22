package com.sivalabs.bookstore.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credit_cards")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cc_id_generator")
    @SequenceGenerator(name = "cc_id_generator", sequenceName = "cc_id_seq", allocationSize = 50)
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
