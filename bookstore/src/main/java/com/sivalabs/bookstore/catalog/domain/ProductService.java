package com.sivalabs.bookstore.catalog.domain;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationProperties properties;

    public PagedResult<Product> getProducts(int pageNo) {
        int page = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(page, properties.pageSize(), Sort.Direction.ASC, "name");
        Page<Product> productsPage = productRepository.findAll(pageable);
        return new PagedResult<>(productsPage);
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code);
    }
}
