package com.sivalabs.bookstore.catalog.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.catalog.CreateProductCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductImporter {
    private final CatalogService catalogService;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Async
    public void importProducts(InputStream inputStream) throws IOException {
        if (productRepository.count() > 0) {
            log.info("Product data already imported.");
            return;
        }

        log.info("Importing product data...");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            reader.lines().map(this::toProduct).forEach(catalogService::createProduct);
        }
        log.info("Product data imported successfully.");
    }

    private CreateProductCommand toProduct(String json) {
        try {
            Book book = objectMapper.readValue(json, Book.class);
            // log.info("Book: {}", book);
            return new CreateProductCommand(
                    "P1000" + book.id(),
                    book.title(),
                    book.description(),
                    book.coverImg(),
                    RandomGenerator.getBigDecimal());
        } catch (Exception e) {
            log.error("Error parsing book: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    record Book(
            Long id, String title, String description, String language, @JsonProperty("cover_img") String coverImg) {}

    record RandomGenerator() {
        static Random r = new Random();
        static int max = 100, min = 10;

        public static int getInt() {
            return r.nextInt(max - min + 1) + min;
        }

        public static BigDecimal getBigDecimal() {
            return new BigDecimal(String.valueOf(getInt()));
        }
    }
}
