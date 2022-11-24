package com.sivalabs.bookstore.catalog;

import com.sivalabs.bookstore.catalog.domain.Product;
import com.sivalabs.bookstore.catalog.domain.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        productRepository.save(new Product(null, "P100", "Beginning SpringBoot", "Beginning SpringBoot 3rd Edition","/p100.png", new BigDecimal("34.0")));
        productRepository.save(new Product(null, "P101", "Clean Code", "Clean Code by Robert C Martin","/p101.png", new BigDecimal("45.40")));
        productRepository.save(new Product(null, "P102", "Refactoring", "Refactoring by Martin Fowler","/p102.png", new BigDecimal("44.50")));
    }
}
