package com.sivalabs.bookstore.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=none",
            "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///bookstore"
        })
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAllInBatch();

        productRepository.save(new Product(null, "P100", "Product 1", "Product 1 desc", null, BigDecimal.TEN));
        productRepository.save(new Product(null, "P101", "Product 2", "Product 2 desc", null, BigDecimal.valueOf(24)));
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
    }

    @Test
    void shouldFailToSaveProductWithDuplicateCode() {
        var product = new Product(null, "P100", "Product name", "Product desc", null, BigDecimal.TEN);
        assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(product));
    }

    @Test
    void shouldGetProductByCode() {
        Optional<Product> optionalProduct = productRepository.findByCode("P100");
        assertThat(optionalProduct).isNotEmpty();
        assertThat(optionalProduct.get().getCode()).isEqualTo("P100");
        assertThat(optionalProduct.get().getName()).isEqualTo("Product 1");
        assertThat(optionalProduct.get().getDescription()).isEqualTo("Product 1 desc");
        assertThat(optionalProduct.get().getPrice()).isEqualTo(BigDecimal.TEN);
    }
}
