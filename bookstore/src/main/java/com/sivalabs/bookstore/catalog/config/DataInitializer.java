package com.sivalabs.bookstore.catalog.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.catalog.application.Commands;
import com.sivalabs.bookstore.catalog.application.ProductCommandHandler;
import com.sivalabs.bookstore.catalog.domain.ProductRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
@Slf4j
class DataInitializer {
    private final ProductCommandHandler productCommandHandler;
    private final ProductRepository productRepository;
    private final ApplicationProperties properties;
    private final ObjectMapper objectMapper;

    @Bean
    ApplicationRunner catalogInitializer(@Value("classpath:/data/books.jsonlines") Resource resource) {
        return args -> importData(resource);
    }

    private void importData(Resource resource) throws IOException {
        if (!properties.dataImportEnabled()) {
            log.info("Data import is disabled.");
            return;
        }
        if (productRepository.count() > 0) {
            log.info("Product data already imported.");
            return;
        }
        log.info("Deleting existing product data...");
        productRepository.deleteAllInBatch();

        log.info("Importing product data...");
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines().map(this::toProduct).forEach(productCommandHandler::handle);
        }
        log.info("Product data imported successfully.");
    }

    private Commands.CreateProductCommand toProduct(String json) {
        try {
            Book book = objectMapper.readValue(json, Book.class);
            log.info("Book: {}", book);
            return new Commands.CreateProductCommand(
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
