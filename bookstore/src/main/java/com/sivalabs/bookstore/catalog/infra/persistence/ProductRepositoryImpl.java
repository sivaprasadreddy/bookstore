package com.sivalabs.bookstore.catalog.infra.persistence;

import com.sivalabs.bookstore.catalog.domain.Product;
import com.sivalabs.bookstore.catalog.domain.ProductRepository;
import com.sivalabs.bookstore.common.model.PagedResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository delegate;

    @Override
    public void saveProduct(Product product) {
        delegate.save(toEntity(product));
    }

    @Override
    public PagedResult<Product> getProducts(int pageNo, int pageSize) {
        int page = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "name");
        Page<Product> productsPage = delegate.findAllBy(pageable);
        return new PagedResult<>(productsPage);
    }

    @Override
    public Optional<Product> findByCode(String code) {
        return delegate.findByCode(code);
    }

    @Override
    public long count() {
        return delegate.count();
    }

    @Override
    public void deleteAllInBatch() {
        delegate.deleteAllInBatch();
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
