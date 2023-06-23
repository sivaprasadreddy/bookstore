package com.sivalabs.bookstore.catalog.domain;

import java.math.BigDecimal;

public class ProductModel {
    private String id;
    private String code;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal salePrice;

    ProductModel(
            String id,
            String code,
            String name,
            String description,
            String imageUrl,
            BigDecimal price,
            BigDecimal discount,
            BigDecimal salePrice) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.discount = discount;
        this.salePrice = salePrice;
    }

    public static ProductModelBuilder builder() {
        return new ProductModelBuilder();
    }

    public String getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getDiscount() {
        return this.discount;
    }

    public BigDecimal getSalePrice() {
        return this.salePrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public static class ProductModelBuilder {
        private String id;
        private String code;
        private String name;
        private String description;
        private String imageUrl;
        private BigDecimal price;
        private BigDecimal discount;
        private BigDecimal salePrice;

        ProductModelBuilder() {}

        public ProductModelBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ProductModelBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ProductModelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductModelBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductModelBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductModelBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductModelBuilder discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public ProductModelBuilder salePrice(BigDecimal salePrice) {
            this.salePrice = salePrice;
            return this;
        }

        public ProductModel build() {
            return new ProductModel(id, code, name, description, imageUrl, price, discount, salePrice);
        }

        public String toString() {
            return "ProductModel.ProductModelBuilder(id="
                    + this.id
                    + ", code="
                    + this.code
                    + ", name="
                    + this.name
                    + ", description="
                    + this.description
                    + ", imageUrl="
                    + this.imageUrl
                    + ", price="
                    + this.price
                    + ", discount="
                    + this.discount
                    + ", salePrice="
                    + this.salePrice
                    + ")";
        }
    }
}
