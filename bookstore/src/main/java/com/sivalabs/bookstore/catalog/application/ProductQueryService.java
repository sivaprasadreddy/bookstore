package com.sivalabs.bookstore.catalog.application;

import com.sivalabs.bookstore.catalog.domain.Product;
import com.sivalabs.bookstore.catalog.domain.ProductRepository;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductQueryService {
    private final ProductRepository productRepository;

    public PagedResult<Product> getProducts(Queries.FindProductsQuery query) {
        return productRepository.getProducts(query.pageNo(), query.pageSize());
    }

    public Optional<Product> getProductByCode(Queries.FindProductByCodeQuery query) {
        return productRepository.findByCode(query.code());
    }
}
