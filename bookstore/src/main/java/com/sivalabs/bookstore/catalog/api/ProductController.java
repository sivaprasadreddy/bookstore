package com.sivalabs.bookstore.catalog.api;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.catalog.core.CatalogService;
import com.sivalabs.bookstore.catalog.core.ProductNotFoundException;
import com.sivalabs.bookstore.catalog.core.models.FindProductsQuery;
import com.sivalabs.bookstore.catalog.core.models.ProductDto;
import com.sivalabs.bookstore.common.model.PagedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final CatalogService catalogService;
    private final ApplicationProperties properties;

    ProductController(CatalogService catalogService, ApplicationProperties properties) {
        this.catalogService = catalogService;
        this.properties = properties;
    }

    @GetMapping
    PagedResult<ProductDto> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        log.info("Fetching products for page: {}", pageNo);
        FindProductsQuery query = new FindProductsQuery(pageNo, properties.pageSize());
        return catalogService.findProducts(query);
    }

    @GetMapping("/{code}")
    ResponseEntity<ProductDto> getProductByCode(@PathVariable String code) {
        log.info("Fetching product by code: {}", code);
        return catalogService
                .findProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.withCode(code));
    }
}
