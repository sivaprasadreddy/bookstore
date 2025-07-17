package com.sivalabs.bookstore.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

@ApplicationModuleTest(
        webEnvironment = RANDOM_PORT,
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config", "users"})
@Sql("/test-books-data.sql")
class CartControllerTests extends AbstractIntegrationTest {

    @Nested
    class AddToCartTests {
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

            assertThat(result).hasStatus(HttpStatus.OK).model().containsKeys("cartItemCount");
        }
    }

    @Nested
    class ShowCartTests {
        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldShowEmptyCart() {
            MockHttpSession session = new MockHttpSession();
            CartUtil.setCart(session, new Cart());

            var result = mockMvcTester.get().uri("/cart").exchange();

            assertThat(session).isNotNull();
            assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .hasViewName("cart")
                    .model()
                    .containsKeys("cart", "orderForm")
                    .satisfies(model -> {
                        var cart = CartUtil.getCart(session);
                        assertThat(cart.isEmpty()).isTrue();
                    });
        }

        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldShowCartWithItems() {
            // First add an item to the cart
            MvcResult addResult = mockMvcTester
                    .post()
                    .uri("/add-to-cart")
                    .with(csrf())
                    .header("HX-Request", "true")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("isbn", "P102")
                    .exchange()
                    .getMvcResult();

            MockHttpSession session = (MockHttpSession) addResult.getRequest().getSession();
            assertThat(session).isNotNull();

            // Then view the cart
            var result = mockMvcTester.get().uri("/cart").session(session).exchange();

            assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .hasViewName("cart")
                    .model()
                    .containsKeys("cart", "orderForm")
                    .satisfies(model -> {
                        var cart = CartUtil.getCart(session);
                        assertThat(cart.isEmpty()).isFalse();
                        assertThat(cart.getItemCount()).isEqualTo(1);
                        assertThat(cart.getItems().getFirst().getIsbn()).isEqualTo("P102");
                    });
        }
    }

    @Nested
    class UpdateCartTests {
        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldUpdateCartItemQuantity() {
            // First add an item to the cart
            MvcResult addResult = mockMvcTester
                    .post()
                    .uri("/add-to-cart")
                    .with(csrf())
                    .header("HX-Request", "true")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("isbn", "P103")
                    .exchange()
                    .getMvcResult();

            MockHttpSession session = (MockHttpSession) addResult.getRequest().getSession();
            assertThat(session).isNotNull();

            // Then update the quantity
            var result = mockMvcTester
                    .post()
                    .uri("/update-cart")
                    .with(csrf())
                    .header("HX-Request", "true")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("isbn", "P103")
                    .param("quantity", "3")
                    .session(session)
                    .exchange();

            assertThat(result).hasStatus(HttpStatus.OK).model().satisfies(model -> {
                var cart = CartUtil.getCart(session);
                assertThat(cart.isEmpty()).isFalse();
                assertThat(cart.getItems().getFirst().getQuantity()).isEqualTo(3);
            });
        }

        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldRemoveItemWhenQuantityIsZero() {
            // First add an item to the cart
            MvcResult addResult = mockMvcTester
                    .post()
                    .uri("/add-to-cart")
                    .with(csrf())
                    .header("HX-Request", "true")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("isbn", "P104")
                    .exchange()
                    .getMvcResult();

            MockHttpSession session = (MockHttpSession) addResult.getRequest().getSession();
            assertThat(session).isNotNull();

            // Then set quantity to 0 to remove it
            var result = mockMvcTester
                    .post()
                    .uri("/update-cart")
                    .with(csrf())
                    .header("HX-Request", "true")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("isbn", "P104")
                    .param("quantity", "0")
                    .session(session)
                    .exchange();

            assertThat(result).hasStatus(HttpStatus.OK).model().satisfies(model -> {
                var cart = CartUtil.getCart(session);
                assertThat(cart).isInstanceOf(Cart.class);
                assertThat(cart.isEmpty()).isTrue();
            });
        }

        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldRefreshViewWhenCartBecomesEmpty() {
            // First add an item to the cart
            MvcResult addResult = mockMvcTester
                    .post()
                    .uri("/add-to-cart")
                    .with(csrf())
                    .header("HX-Request", "true")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("isbn", "P105")
                    .exchange()
                    .getMvcResult();

            MockHttpSession session = (MockHttpSession) addResult.getRequest().getSession();
            assertThat(session).isNotNull();

            // Then set quantity to 0 to remove it and make cart empty
            var result = mockMvcTester
                    .post()
                    .uri("/update-cart")
                    .with(csrf())
                    .header("HX-Request", "true")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("isbn", "P105")
                    .param("quantity", "0")
                    .session(session)
                    .exchange();

            // Check for HX-Refresh header
            MvcResult refreshMvcResult = result.getMvcResult();
            String hxRefreshHeader = refreshMvcResult.getResponse().getHeader("HX-Refresh");
            assertThat(hxRefreshHeader).isEqualTo("true");

            assertThat(result).hasStatus(HttpStatus.OK);
        }
    }
}
