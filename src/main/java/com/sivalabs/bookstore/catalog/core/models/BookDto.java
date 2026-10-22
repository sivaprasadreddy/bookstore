package com.sivalabs.bookstore.catalog.core.models;

import java.math.BigDecimal;

public class BookDto {
    private Long id;
    private String isbn;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;

    public BookDto(Long id, String isbn, String name, String description, String imageUrl, BigDecimal price) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDisplayName() {
        if (name.length() <= 20) {
            return name;
        }
        return name.substring(0, 20) + "...";
    }
}
