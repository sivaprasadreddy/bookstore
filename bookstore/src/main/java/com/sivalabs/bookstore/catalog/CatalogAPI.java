package com.sivalabs.bookstore.catalog;

import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;

public interface CatalogAPI {

    void createProduct(CreateProductCommand cmd);

    PagedResult<Product> findProducts(FindProductsQuery query);

    Optional<Product> findProductByCode(String code);
}
