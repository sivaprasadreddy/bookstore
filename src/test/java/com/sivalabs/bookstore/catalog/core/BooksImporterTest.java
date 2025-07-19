package com.sivalabs.bookstore.catalog.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(
        webEnvironment = RANDOM_PORT,
        extraIncludes = {"config"})
@Sql("/test-books-data.sql")
class BooksImporterTest extends AbstractIntegrationTest {

    @Autowired
    private BooksImporter booksImporter;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldImportBooksFromJsonlinesFile() throws IOException {
        bookRepository.deleteAllInBatch();

        InputStream inputStream = new ClassPathResource("test-books-50.jsonlines").getInputStream();
        booksImporter.importBooks(inputStream);

        assertThat(bookRepository.count()).isEqualTo(50L);
    }

    @Test
    void shouldNotImportIfAlreadyBooksExist() throws IOException {
        InputStream inputStream = new ClassPathResource("test-books-50.jsonlines").getInputStream();
        booksImporter.importBooks(inputStream);

        assertThat(bookRepository.count()).isEqualTo(15L);
    }
}
