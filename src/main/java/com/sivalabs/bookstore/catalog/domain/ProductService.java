package com.sivalabs.bookstore.catalog.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        Pageable pageable = PageRequest.of(0, 120, Sort.Direction.ASC, "name");
        return productRepository.findAll(pageable).getContent();
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code);
    }
}
