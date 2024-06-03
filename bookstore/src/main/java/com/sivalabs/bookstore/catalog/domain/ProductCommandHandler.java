package com.sivalabs.bookstore.catalog.domain;

import com.sivalabs.bookstore.catalog.Commands;
import com.sivalabs.bookstore.catalog.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductCommandHandler {
    private final ProductRepository productRepository;

    public void handle(Commands.CreateProductCommand cmd) {
        Product product = new Product(null, cmd.code(), cmd.name(), cmd.description(), cmd.imageUrl(), cmd.price());
        productRepository.saveProduct(product);
    }
}
