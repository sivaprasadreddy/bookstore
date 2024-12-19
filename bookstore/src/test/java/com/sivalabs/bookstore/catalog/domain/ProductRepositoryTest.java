package com.sivalabs.bookstore.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sivalabs.bookstore.catalog.Product;
import com.sivalabs.bookstore.common.TestcontainersConfiguration;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAllInBatch();

        productRepository.save(new ProductEntity(null, "P100", "Product 1", "Product 1 desc", null, BigDecimal.TEN));
        productRepository.save(
                new ProductEntity(null, "P101", "Product 2", "Product 2 desc", null, BigDecimal.valueOf(24)));
    }

    @Test
    void shouldFailToSaveProductWithDuplicateCode() {
        var product = new ProductEntity(null, "P100", "Product name", "Product desc", null, BigDecimal.TEN);
        assertThrows(DataIntegrityViolationException.class, () -> productRepository.saveAndFlush(product));
    }

    @Test
    void shouldGetProductByCode() {
        Optional<Product> optionalProduct = productRepository.findByCode("P100");
        assertThat(optionalProduct).isNotEmpty();
        assertThat(optionalProduct.get().code()).isEqualTo("P100");
        assertThat(optionalProduct.get().name()).isEqualTo("Product 1");
        assertThat(optionalProduct.get().description()).isEqualTo("Product 1 desc");
        assertThat(optionalProduct.get().price()).isEqualTo(BigDecimal.TEN);
    }
}
