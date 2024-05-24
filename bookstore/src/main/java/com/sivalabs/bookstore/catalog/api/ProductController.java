package com.sivalabs.bookstore.catalog.api;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.catalog.application.ProductQueryService;
import com.sivalabs.bookstore.catalog.application.Queries;
import com.sivalabs.bookstore.catalog.domain.Product;
import com.sivalabs.bookstore.catalog.domain.ProductNotFoundException;
import com.sivalabs.bookstore.common.model.PagedResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
class ProductController {
    private final ProductQueryService productQueryService;
    private final ApplicationProperties properties;

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        log.info("Fetching products for page: {}", pageNo);
        Queries.FindProductsQuery query = new Queries.FindProductsQuery(pageNo, properties.pageSize());
        return productQueryService.getProducts(query);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        log.info("Fetching product by code: {}", code);
        Queries.FindProductByCodeQuery query = new Queries.FindProductByCodeQuery(code);
        return productQueryService
                .getProductByCode(query)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.withCode(code));
    }
}
