package com.sivalabs.bookstore.common.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class LineItem implements Serializable {
    private String isbn;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private int quantity;

    public LineItem() {}

    public LineItem(String isbn, String name, BigDecimal price, String imageUrl, int quantity) {
        this.isbn = isbn;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
