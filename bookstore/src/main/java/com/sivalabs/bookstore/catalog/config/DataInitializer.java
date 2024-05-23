package com.sivalabs.bookstore.catalog.config;

import com.sivalabs.bookstore.catalog.domain.ProductDataImporter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
class DataInitializer {
    private final ProductDataImporter productDataImporter;

    @Bean
    ApplicationRunner catalogInitializer(@Value("classpath:/data/books.jsonlines") Resource resource) {
        return args -> productDataImporter.importData(resource);
    }
}
