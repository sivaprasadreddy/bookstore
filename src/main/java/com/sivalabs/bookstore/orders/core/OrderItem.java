package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.common.model.LineItem;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_id_generator")
    @SequenceGenerator(name = "order_item_id_generator", sequenceName = "order_item_id_seq")
    private Long id;

    @Embedded
    @AttributeOverrides(
            value = {
                @AttributeOverride(name = "isbn", column = @Column(name = "isbn", nullable = false)),
                @AttributeOverride(name = "name", column = @Column(name = "name", nullable = false)),
                @AttributeOverride(name = "price", column = @Column(name = "price", nullable = false)),
                @AttributeOverride(name = "imageUrl", column = @Column(name = "image_url")),
                @AttributeOverride(name = "quantity", column = @Column(name = "quantity", nullable = false))
            })
    private LineItem lineItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    public Long getId() {
        return this.id;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
