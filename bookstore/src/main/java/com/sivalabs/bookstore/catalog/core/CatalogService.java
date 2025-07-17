package com.sivalabs.bookstore.catalog.core;

import com.sivalabs.bookstore.catalog.core.models.CreateProductCommand;
import com.sivalabs.bookstore.catalog.core.models.FindProductsQuery;
import com.sivalabs.bookstore.catalog.core.models.ProductDto;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogService {
    private final ProductRepository productRepository;

    CatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void createProduct(CreateProductCommand cmd) {
        ProductDto product =
                new ProductDto(null, cmd.code(), cmd.name(), cmd.description(), cmd.imageUrl(), cmd.price());
        productRepository.save(toEntity(product));
    }

    @Transactional(readOnly = true)
    public PagedResult<ProductDto> findProducts(FindProductsQuery query) {
        int pageNo = query.pageNo();
        int pageSize = query.pageSize();
        int page = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "name");
        Page<ProductDto> productsPage = productRepository.findAllBy(pageable);
        return new PagedResult<>(productsPage);
    }

    @Transactional(readOnly = true)
    public Optional<ProductDto> findProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    private ProductEntity toEntity(ProductDto product) {
        return new ProductEntity(
                product.id(),
                product.code(),
                product.name(),
                product.description(),
                product.imageUrl(),
                product.price());
    }
}
