package com.sivalabs.bookstore.catalog;

import com.sivalabs.bookstore.catalog.core.CatalogService;
import com.sivalabs.bookstore.catalog.core.models.ProductDto;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CatalogAPI {

    private final CatalogService catalogService;

    CatalogAPI(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public Optional<ProductDto> findProductByCode(String code) {
        return catalogService.findProductByCode(code);
    }
}
