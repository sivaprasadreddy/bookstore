package com.sivalabs.bookstore.catalog.core;

import com.sivalabs.bookstore.catalog.core.models.ProductDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductDto> findByCode(String code);

    Page<ProductDto> findAllBy(Pageable pageable);
}
