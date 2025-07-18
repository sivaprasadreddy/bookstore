package com.sivalabs.bookstore.catalog.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.bookstore.catalog.core.models.CreateBookCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BooksImporter {
    private static final Logger log = LoggerFactory.getLogger(BooksImporter.class);
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    BooksImporter(BookService bookService, BookRepository bookRepository, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.objectMapper = objectMapper;
    }

    @Async
    public void importBooks(InputStream inputStream) throws IOException {
        if (bookRepository.count() > 0) {
            log.info("Book data already imported.");
            return;
        }

        log.info("Importing book data...");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            reader.lines().map(this::toBook).forEach(bookService::createBook);
        }
        log.info("Book data imported successfully.");
    }

    private CreateBookCommand toBook(String json) {
        try {
            Book book = objectMapper.readValue(json, Book.class);
            return new CreateBookCommand(
                    "P1000" + book.id(),
                    book.title(),
                    book.description(),
                    book.coverImg(),
                    RandomGenerator.getBigDecimal());
        } catch (Exception e) {
            throw new BooksImportException("Error parsing book: {}", e);
        }
    }

    record Book(
            Long id, String title, String description, String language, @JsonProperty("cover_img") String coverImg) {}

    record RandomGenerator() {
        static SecureRandom r = new SecureRandom();
        static int max = 100, min = 10;

        public static int getInt() {
            return r.nextInt(max - min + 1) + min;
        }

        public static BigDecimal getBigDecimal() {
            return new BigDecimal(String.valueOf(getInt()));
        }
    }
}
