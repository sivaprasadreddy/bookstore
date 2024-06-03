package com.sivalabs.bookstore.catalog;

import com.sivalabs.bookstore.catalog.Queries.FindProductByCodeQuery;
import com.sivalabs.bookstore.catalog.Queries.FindProductsQuery;
import com.sivalabs.bookstore.catalog.domain.ProductCommandHandler;
import com.sivalabs.bookstore.catalog.domain.ProductQueryHandler;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CatalogAPI {
    private final ProductCommandHandler productCommandHandler;
    private final ProductQueryHandler productQueryHandler;

    public void createProduct(Commands.CreateProductCommand cmd) {
        productCommandHandler.handle(cmd);
    }

    public PagedResult<Product> findProducts(FindProductsQuery query) {
        return productQueryHandler.getProducts(query);
    }

    public Optional<Product> findProductByCode(FindProductByCodeQuery query) {
        return productQueryHandler.getProductByCode(query);
    }
}
