package com.sivalabs.bookstore.catalog.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.ApplicationProperties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.random.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductDataImporter {
    private final ProductRepository productRepository;
    private final ApplicationProperties properties;
    private final ObjectMapper objectMapper;

    @Async
    public void importData(Resource resource) throws Exception {
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                createProduct(line);
            }
        }
        log.info("Product data imported successfully.");
    }

    private void createProduct(String json) throws JsonProcessingException {
        Book book = objectMapper.readValue(json, Book.class);
        Product product = new Product();
        product.setCode("P1000" + book.id());
        product.setName(book.title());
        product.setDescription(book.description());
        product.setPrice(
                new BigDecimal(String.valueOf(RandomGenerator.getDefault().nextInt(10, 80))));
        product.setImageUrl(book.coverImg());
        productRepository.save(product);
    }

    record Book(
            Long id, String title, String description, String language, @JsonProperty("cover_img") String coverImg) {}
}
