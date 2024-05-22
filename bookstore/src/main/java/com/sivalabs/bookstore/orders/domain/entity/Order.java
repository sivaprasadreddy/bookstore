package com.sivalabs.bookstore.orders.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Setter
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_seq", allocationSize = 50)
    private Long id;

    private String orderId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private Set<OrderItem> items;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String deliveryAddressLine1;

    private String deliveryAddressLine2;

    @Column(nullable = false)
    private String deliveryAddressCity;

    @Column(nullable = false)
    private String deliveryAddressState;

    @Column(nullable = false)
    private String deliveryAddressZipCode;

    @Column(nullable = false)
    private String deliveryAddressCountry;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String comments;
}
