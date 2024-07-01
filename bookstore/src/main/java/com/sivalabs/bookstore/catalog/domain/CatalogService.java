package com.sivalabs.bookstore.catalog.domain;

import com.sivalabs.bookstore.catalog.CatalogAPI;
import com.sivalabs.bookstore.catalog.CreateProductCommand;
import com.sivalabs.bookstore.catalog.FindProductsQuery;
import com.sivalabs.bookstore.catalog.Product;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
class CatalogService implements CatalogAPI {
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public void createProduct(CreateProductCommand cmd) {
        Product product = new Product(null, cmd.code(), cmd.name(), cmd.description(), cmd.imageUrl(), cmd.price());
        productRepository.save(toEntity(product));
    }

    @Override
    public PagedResult<Product> findProducts(FindProductsQuery query) {
        int pageNo = query.pageNo();
        int pageSize = query.pageSize();
        int page = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "name");
        Page<Product> productsPage = productRepository.findAllBy(pageable);
        return new PagedResult<>(productsPage);
    }

    @Override
    public Optional<Product> findProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.id(),
                product.code(),
                product.name(),
                product.description(),
                product.imageUrl(),
                product.price());
    }
}
