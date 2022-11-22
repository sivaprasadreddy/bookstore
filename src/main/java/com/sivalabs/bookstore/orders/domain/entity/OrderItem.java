package com.sivalabs.bookstore.orders.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @SequenceGenerator(name = "order_item_id_generator", sequenceName = "order_item_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "order_item_id_generator")
    private Long id;

    @Column(nullable = false)
    private String productCode;
    private String productName;
    @Column(nullable = false)
    private BigDecimal productPrice;
    @Column(nullable = false)
    private Integer quantity;
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    public BigDecimal getSubTotal() {
        return productPrice.multiply(new BigDecimal(quantity));
    }
}
