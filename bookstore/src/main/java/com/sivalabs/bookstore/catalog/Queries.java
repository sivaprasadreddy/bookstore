package com.sivalabs.bookstore.catalog;

public record Queries() {

    public record FindProductsQuery(int pageNo, int pageSize) {}

    public record FindProductByCodeQuery(String code) {}
}
