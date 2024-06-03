package com.sivalabs.bookstore.catalog.domain;

import com.sivalabs.bookstore.catalog.Product;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;

public interface ProductRepository {
    void saveProduct(Product product);

    PagedResult<Product> getProducts(int pageNo, int pageSize);

    Optional<Product> findByCode(String code);

    long count();

    void deleteAllInBatch();
}
