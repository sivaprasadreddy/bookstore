package com.sivalabs.bookstore.catalog.infra.persistence;

import com.sivalabs.bookstore.catalog.domain.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<Product> findByCode(String code);

    Page<Product> findAllBy(Pageable pageable);
}
