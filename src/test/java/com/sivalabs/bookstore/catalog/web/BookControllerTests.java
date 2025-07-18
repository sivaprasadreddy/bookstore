package com.sivalabs.bookstore.catalog.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.model.PagedResult;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(
        webEnvironment = RANDOM_PORT,
        extraIncludes = {"config"})
@Sql("/test-books-data.sql")
class BookControllerTests extends AbstractIntegrationTest {

    @Test
    void shouldGetAllBooks() {
        var result = mockMvcTester.get().uri("/books").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("catalog/books")
                .model()
                .containsKeys("booksPage")
                .satisfies(model -> {
                    var pagedResult = model.get("booksPage");
                    assertThat(pagedResult).isInstanceOf(PagedResult.class);

                    @SuppressWarnings("unchecked")
                    PagedResult<BookDto> booksPage = (PagedResult<BookDto>) pagedResult;
                    assertThat(booksPage.data()).isNotEmpty();
                    assertThat(booksPage.totalElements()).isEqualTo(15);
                });
    }

    @Test
    void shouldGetBookByIsbn() {
        var result = mockMvcTester.get().uri("/books/P100").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("catalog/book-details")
                .model()
                .containsKeys("book")
                .satisfies(model -> {
                    var book = model.get("book");
                    assertThat(book).isInstanceOf(BookDto.class);

                    BookDto bookDto = (BookDto) book;
                    assertThat(bookDto.isbn()).isEqualTo("P100");
                    assertThat(bookDto.name()).isEqualTo("The Hunger Games");
                });
    }

    @Test
    void shouldReturn404WhenBookNotFound() {
        var result = mockMvcTester.get().uri("/books/NONEXISTENT").exchange();

        assertThat(result).hasStatus(HttpStatus.OK).hasViewName("error/404");
    }
}
