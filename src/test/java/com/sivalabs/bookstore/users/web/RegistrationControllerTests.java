package com.sivalabs.bookstore.users.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.annotation.DirtiesContext;

@ApplicationModuleTest(
        webEnvironment = RANDOM_PORT,
        extraIncludes = {"config"})
class RegistrationControllerTests extends AbstractIntegrationTest {

    @Test
    void shouldShowRegistrationForm() {
        var result = mockMvcTester.get().uri("/registration").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("users/registration")
                .model()
                .containsKey("user");
    }

    @Test
    @DirtiesContext
    void shouldRegisterUserSuccessfully() {
        var result = mockMvcTester
                .post()
                .uri("/registration")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Test User")
                .param("email", "test@example.com")
                .param("password", "password")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.FOUND).hasHeader("Location", "/registration/success");
    }

    @Test
    void shouldReturnToRegistrationFormWhenValidationErrors() {
        var result = mockMvcTester
                .post()
                .uri("/registration")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "")
                .param("email", "invalid-email")
                .param("password", "pwd")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("users/registration")
                .model()
                .extractingBindingResult("user")
                .hasErrorsCount(3)
                .hasFieldErrors("name", "email", "password");
    }

    @Test
    @DirtiesContext
    void shouldRedirectToRegistrationWithErrorMessageWhenDuplicateEmail() {
        var result = mockMvcTester
                .post()
                .uri("/registration")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Another User")
                .param("email", "siva@gmail.com")
                .param("password", "password")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasHeader("Location", "/registration")
                .flash()
                .containsKey("errorMessage");
    }

    @Test
    void shouldShowRegistrationSuccessPage() {
        var result = mockMvcTester.get().uri("/registration/success").exchange();

        assertThat(result).hasStatus(HttpStatus.OK).hasViewName("users/registration-status");
    }
}
