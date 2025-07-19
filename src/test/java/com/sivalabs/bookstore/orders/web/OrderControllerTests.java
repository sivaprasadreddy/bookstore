package com.sivalabs.bookstore.orders.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.sivalabs.bookstore.cart.CartUtil;
import com.sivalabs.bookstore.cart.core.models.Cart;
import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.common.model.LineItem;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderStatus;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import java.math.BigDecimal;
import java.util.List;
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
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config"},
        webEnvironment = RANDOM_PORT)
@Sql({"/test-books-data.sql", "/test-orders-data.sql"})
class OrderControllerTests extends AbstractIntegrationTest {
    @Nested
    class CreateOrderApiTests {
        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldCreateOrderSuccessfully() {
            MockHttpSession session = new MockHttpSession();
            Cart cart = new Cart();
            cart.addItem(new LineItem(
                    "P100", "The Hunger Games", new BigDecimal("34.0"), "https://images.bookstore.com/p100.png", 1));
            CartUtil.setCart(session, cart);

            // Create a valid order form
            Customer customer = new Customer("Test User", "test@example.com", "1234567890");
            Address address = new Address("123 Main St", "Apt 4B", "New York", "NY", "10001", "USA");

            // Submit the order
            var result = mockMvcTester
                    .post()
                    .uri("/orders")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("customer.name", customer.name())
                    .param("customer.email", customer.email())
                    .param("customer.phone", customer.phone())
                    .param("deliveryAddress.addressLine1", address.addressLine1())
                    .param("deliveryAddress.addressLine2", address.addressLine2())
                    .param("deliveryAddress.city", address.city())
                    .param("deliveryAddress.state", address.state())
                    .param("deliveryAddress.zipCode", address.zipCode())
                    .param("deliveryAddress.country", address.country())
                    .session(session)
                    .exchange();

            // Verify the response is a redirect to the order details page
            assertThat(result).hasStatus(HttpStatus.FOUND);

            // Check the redirect location
            MvcResult mvcResult = result.getMvcResult();
            String location = mvcResult.getResponse().getHeader("Location");
            assertThat(location).startsWith("/orders/");

            // Verify the cart is cleared
            cart = CartUtil.getCart(session);
            assertThat(cart.isEmpty()).isTrue();
        }

        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
            MockHttpSession session = new MockHttpSession();
            Cart cart = new Cart();
            cart.addItem(new LineItem(
                    "P100", "The Hunger Games", new BigDecimal("34.0"), "https://images.bookstore.com/p100.png", 1));
            CartUtil.setCart(session, cart);

            // Submit the order with missing mandatory fields
            var result = mockMvcTester
                    .post()
                    .uri("/orders")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("customer.name", "") // Empty name (required)
                    .param("customer.email", "invalid-email") // Invalid email
                    .param("customer.phone", "1234567890")
                    .param("deliveryAddress.addressLine1", "123 Main St")
                    .param("deliveryAddress.addressLine2", "Apt 4B")
                    .param("deliveryAddress.city", "") // Empty city (required)
                    .param("deliveryAddress.state", "NY")
                    .param("deliveryAddress.zipCode", "10001")
                    .param("deliveryAddress.country", "USA")
                    .session(session)
                    .exchange();

            // Verify the response is a bad request (validation error)
            assertThat(result)
                    .hasStatus(HttpStatus.OK) // Spring MVC returns 200 OK with validation errors in the model
                    .model()
                    .extractingBindingResult("orderForm")
                    .hasErrorsCount(3)
                    .hasFieldErrors("customer.name", "customer.email", "deliveryAddress.city");
        }
    }

    @Nested
    class CheckoutTests {
        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldRenderCheckoutPageWhenCartHasItems() {
            MockHttpSession session = new MockHttpSession();
            Cart cart = new Cart();
            cart.addItem(new LineItem(
                    "P100", "The Hunger Games", new BigDecimal("34.0"), "https://images.bookstore.com/p100.png", 1));
            CartUtil.setCart(session, cart);

            var result =
                    mockMvcTester.get().uri("/orders/checkout").session(session).exchange();

            assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .hasViewName("orders/checkout")
                    .model()
                    .containsKeys("orderForm")
                    .satisfies(model -> {
                        var orderForm = (OrderForm) model.get("orderForm");
                        assertThat(orderForm).isNotNull();
                    });
        }

        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldRedirectToCartWhenCartIsEmpty() {
            MockHttpSession session = new MockHttpSession();
            Cart cart = new Cart(); // Empty cart
            CartUtil.setCart(session, cart);

            var result =
                    mockMvcTester.get().uri("/orders/checkout").session(session).exchange();

            assertThat(result).hasStatus(HttpStatus.FOUND);

            MvcResult mvcResult = result.getMvcResult();
            String location = mvcResult.getResponse().getHeader("Location");
            assertThat(location).isEqualTo("/cart");
        }

        @Test
        void shouldRequireAuthenticationForCheckout() {
            // No @WithUserDetails annotation means unauthenticated request
            MockHttpSession session = new MockHttpSession();
            Cart cart = new Cart();
            cart.addItem(new LineItem(
                    "P100", "The Hunger Games", new BigDecimal("34.0"), "https://images.bookstore.com/p100.png", 1));
            CartUtil.setCart(session, cart);

            var result =
                    mockMvcTester.get().uri("/orders/checkout").session(session).exchange();

            // Should redirect to login page
            assertThat(result).hasStatus(HttpStatus.FOUND);

            MvcResult mvcResult = result.getMvcResult();
            String location = mvcResult.getResponse().getHeader("Location");
            assertThat(location).contains("/login");
        }
    }

    @Nested
    class GetOrderApiTests {
        String orderNumber = "order-123";

        @Test
        @WithUserDetails("admin@gmail.com") // User ID 1 in test data
        void shouldGetOrderSuccessfully() {
            var result = mockMvcTester
                    .get()
                    .uri("/orders/{orderNumber}", orderNumber)
                    .exchange();

            assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .hasViewName("orders/order-details")
                    .model()
                    .containsKeys("order")
                    .satisfies(model -> {
                        var order = (OrderDto) model.get("order");
                        assertThat(order).isNotNull();
                        assertThat(order.orderNumber()).isEqualTo(orderNumber);
                        assertThat(order.userId()).isEqualTo(1L);
                        assertThat(order.items()).hasSize(2);
                    });
        }

        @Test
        @WithUserDetails("admin@gmail.com")
        void shouldReturnNotFoundWhenOrderNumberNotExist() {
            String nonExistentOrderNumber = "non-existent-order";

            var result = mockMvcTester
                    .get()
                    .uri("/orders/{orderNumber}", nonExistentOrderNumber)
                    .exchange();

            assertThat(result).hasStatus(HttpStatus.OK).hasViewName("error/404");
        }

        @Test
        @WithUserDetails("admin@gmail.com") // User ID 1 in test data
        void shouldReturnNotFoundWhenOrderBelongsToDifferentUser() {
            String orderNumberBelongingToAnotherUser = "order-456"; // Belongs to user ID 2

            var result = mockMvcTester
                    .get()
                    .uri("/orders/{orderNumber}", orderNumberBelongingToAnotherUser)
                    .exchange();

            assertThat(result).hasStatus(HttpStatus.OK).hasViewName("error/404");
        }

        @Test
        void shouldRequireAuthenticationForViewingOrder() {
            // No @WithUserDetails annotation means unauthenticated request
            var result = mockMvcTester
                    .get()
                    .uri("/orders/{orderNumber}", orderNumber)
                    .exchange();

            // Should redirect to login page
            assertThat(result).hasStatus(HttpStatus.FOUND);

            MvcResult mvcResult = result.getMvcResult();
            String location = mvcResult.getResponse().getHeader("Location");
            assertThat(location).contains("/login");
        }
    }

    @Nested
    class GetOrdersTests {
        @Test
        @WithUserDetails("admin@gmail.com") // User ID 1 in test data
        void shouldListUserOrders() {
            var result = mockMvcTester.get().uri("/orders").exchange();

            assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .hasViewName("orders/orders")
                    .model()
                    .containsKeys("orders")
                    .satisfies(model -> {
                        @SuppressWarnings("unchecked")
                        List<OrderSummary> orders = (List<OrderSummary>) model.get("orders");
                        assertThat(orders).isNotNull();
                        assertThat(orders).hasSize(1); // User ID 1 has one order in test data

                        // Verify the order details
                        OrderSummary orderSummary = orders.getFirst();
                        assertThat(orderSummary.orderNumber()).isEqualTo("order-123");
                        assertThat(orderSummary.status()).isEqualTo(OrderStatus.NEW);
                    });
        }

        @Test
        void shouldRequireAuthenticationForListingOrders() {
            // No @WithUserDetails annotation means unauthenticated request
            var result = mockMvcTester.get().uri("/orders").exchange();

            // Should redirect to login page
            assertThat(result).hasStatus(HttpStatus.FOUND);

            MvcResult mvcResult = result.getMvcResult();
            String location = mvcResult.getResponse().getHeader("Location");
            assertThat(location).contains("/login");
        }
    }

    @Nested
    class CreateOrderAuthenticationTests {
        @Test
        void shouldRequireAuthenticationForCreatingOrder() {
            // No @WithUserDetails annotation means unauthenticated request
            var result = mockMvcTester
                    .post()
                    .uri("/orders")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .exchange();

            // Should redirect to login page
            assertThat(result).hasStatus(HttpStatus.FOUND);

            MvcResult mvcResult = result.getMvcResult();
            String location = mvcResult.getResponse().getHeader("Location");
            assertThat(location).contains("/login");
        }
    }
}
