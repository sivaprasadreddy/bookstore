package com.sivalabs.bookstore.cart;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ApplicationModuleTest(webEnvironment = RANDOM_PORT,
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config"})
@Sql("/test-books-data.sql")
class CartControllerTests extends AbstractIntegrationTest {

    @Test
    void shouldAddBookToCart() {
        var result = mockMvcTester
                .post()
                .uri("/add-to-cart")
                .with(csrf())
                .header("HX-Request", "true") // HTMX request header
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "P100")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .model()
                .containsKeys("cartItemCount");
    }
}