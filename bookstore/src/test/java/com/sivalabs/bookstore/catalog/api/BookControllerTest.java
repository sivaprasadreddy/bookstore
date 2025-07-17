package com.sivalabs.bookstore.catalog.api;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(webEnvironment = RANDOM_PORT)
@Sql("/test-books-data.sql")
class BookControllerTest extends AbstractIntegrationTest {

    @Test
    void shouldGetAllBooks() {}

    @Test
    void shouldGetBookByIsbn() {}

    @Test
    void shouldReturnNotFoundWhenBookIsbnNotExists() {}
}
