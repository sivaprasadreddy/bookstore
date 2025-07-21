package com.sivalabs.bookstore.catalog.config;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.catalog.core.BooksImporter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

@Configuration
class DataInitializer {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final BooksImporter booksImporter;
    private final ApplicationProperties properties;

    DataInitializer(BooksImporter booksImporter, ApplicationProperties properties) {
        this.booksImporter = booksImporter;
        this.properties = properties;
    }

    @Bean
    ApplicationRunner catalogInitializer() {
        return args -> importData();
    }

    private void importData() throws IOException {
        if (!properties.dataImportEnabled()) {
            log.info("Data import is disabled.");
            return;
        }
        String importFile = properties.dataImportFile();
        if (!StringUtils.hasLength(importFile)) {
            log.info("Import file is empty. Skipping import.");
            return;
        }
        Resource resource = new ClassPathResource(importFile);
        booksImporter.importBooksAsync(resource.getInputStream());
    }
}
