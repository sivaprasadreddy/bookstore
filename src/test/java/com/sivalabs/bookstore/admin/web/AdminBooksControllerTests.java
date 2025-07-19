package com.sivalabs.bookstore.admin.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.model.PagedResult;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(
        webEnvironment = RANDOM_PORT,
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config", "users"})
@Sql("/test-books-data.sql")
class AdminBooksControllerTests extends AbstractIntegrationTest {

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowBooksList() {
        var result = mockMvcTester.get().uri("/admin/books").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/books")
                .model()
                .containsKeys("pagedResult", "pagination")
                .satisfies(model -> {
                    var pagedResult = model.get("pagedResult");
                    assertThat(pagedResult).isInstanceOf(PagedResult.class);

                    @SuppressWarnings("unchecked")
                    PagedResult<BookDto> booksPage = (PagedResult<BookDto>) pagedResult;
                    assertThat(booksPage.data()).isNotEmpty();
                    assertThat(booksPage.totalElements()).isEqualTo(15);
                });
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowBooksList_WithPagination() {
        var result = mockMvcTester.get().uri("/admin/books?page=2&size=5").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/books")
                .model()
                .containsKeys("pagedResult", "pagination")
                .satisfies(model -> {
                    var pagedResult = model.get("pagedResult");
                    assertThat(pagedResult).isInstanceOf(PagedResult.class);

                    @SuppressWarnings("unchecked")
                    PagedResult<BookDto> booksPage = (PagedResult<BookDto>) pagedResult;
                    assertThat(booksPage.data()).isNotEmpty();
                });
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowEditBookForm() {
        var result = mockMvcTester.get().uri("/admin/books/P100/edit").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/edit-book")
                .model()
                .containsKeys("bookForm")
                .satisfies(model -> {
                    var bookForm = model.get("bookForm");
                    assertThat(bookForm).isInstanceOf(BookForm.class);

                    BookForm form = (BookForm) bookForm;
                    assertThat(form.isbn()).isEqualTo("P100");
                    assertThat(form.name()).isEqualTo("The Hunger Games");
                });
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldReturn404WhenBookNotFound() {
        var result = mockMvcTester.get().uri("/admin/books/NONEXISTENT/edit").exchange();

        assertThat(result).hasStatus(HttpStatus.OK).hasViewName("error/404");
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldUpdateBook_Success() {
        var result = mockMvcTester
                .put()
                .uri("/admin/books/P100")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("isbn", "P100")
                .param("name", "Updated Book Name")
                .param("description", "Updated description")
                .param("imageUrl", "https://example.com/image.jpg")
                .param("price", "39.99")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasRedirectedUrl("/admin/books")
                .flash()
                .containsKey("message");
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldNotUpdateBook_ValidationErrors() {
        var result = mockMvcTester
                .put()
                .uri("/admin/books/P100")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("isbn", "P100")
                .param("name", "") // Empty name (required)
                .param("description", "Updated description")
                .param("imageUrl", "https://example.com/image.jpg")
                .param("price", "-10") // Negative price (invalid)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/edit-book")
                .model()
                .extractingBindingResult("bookForm")
                .hasErrorsCount(2)
                .hasFieldErrors("name", "price");
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldHandleException_WhenUpdateFails() {
        // Using a non-existent book ID to trigger an exception
        var result = mockMvcTester
                .put()
                .uri("/admin/books/P999")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "999")
                .param("isbn", "P999")
                .param("name", "Test Book")
                .param("description", "Test description")
                .param("imageUrl", "https://example.com/image.jpg")
                .param("price", "29.99")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/admin/books")
                .flash()
                .containsKey("error");
    }
}
