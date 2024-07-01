package com.sivalabs.bookstore.catalog.config;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.catalog.domain.ProductImporter;
import java.io.IOException;
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
    private final ProductImporter productImporter;
    private final ApplicationProperties properties;

    @Bean
    ApplicationRunner catalogInitializer(@Value("classpath:/data/books.jsonlines") Resource resource) {
        return args -> importData(resource);
    }

    private void importData(Resource resource) throws IOException {
        if (!properties.dataImportEnabled()) {
            log.info("Data import is disabled.");
            return;
        }
        productImporter.importProducts(resource.getInputStream());
    }
}
