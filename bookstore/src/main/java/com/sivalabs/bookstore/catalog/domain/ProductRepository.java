package com.sivalabs.bookstore.catalog.domain;

import com.sivalabs.bookstore.catalog.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<Product> findByCode(String code);

    Page<Product> findAllBy(Pageable pageable);
}
