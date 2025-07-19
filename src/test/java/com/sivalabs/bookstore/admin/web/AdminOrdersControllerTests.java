package com.sivalabs.bookstore.admin.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.common.model.PagedResult;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderStatus;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(
        webEnvironment = RANDOM_PORT,
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config", "users", "orders"})
@Sql({"/test-books-data.sql", "/test-orders-data.sql"})
class AdminOrdersControllerTests extends AbstractIntegrationTest {

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowOrdersList() {
        var result = mockMvcTester.get().uri("/admin/orders").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/orders")
                .model()
                .containsKeys("pagedResult", "pagination")
                .satisfies(model -> {
                    var pagedResult = model.get("pagedResult");
                    assertThat(pagedResult).isInstanceOf(PagedResult.class);

                    @SuppressWarnings("unchecked")
                    PagedResult<OrderSummary> ordersPage = (PagedResult<OrderSummary>) pagedResult;
                    assertThat(ordersPage.data()).isNotEmpty();
                });
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowOrdersList_WithPagination() {
        var result = mockMvcTester.get().uri("/admin/orders?page=2&size=5").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/orders")
                .model()
                .containsKeys("pagedResult", "pagination")
                .satisfies(model -> {
                    var pagedResult = model.get("pagedResult");
                    assertThat(pagedResult).isInstanceOf(PagedResult.class);

                    @SuppressWarnings("unchecked")
                    PagedResult<OrderSummary> ordersPage = (PagedResult<OrderSummary>) pagedResult;
                    // Since there are only a few orders in the test data,
                    // page 2 with size 5 should be empty
                    assertThat(ordersPage.data()).isEmpty();
                    // Verify other pagination properties
                    assertThat(ordersPage.isFirstPage()).isFalse();
                    assertThat(ordersPage.isLastPage()).isTrue();
                    assertThat(ordersPage.hasNextPage()).isFalse();
                    assertThat(ordersPage.hasPreviousPage()).isTrue();
                });
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowOrderDetails() {
        String orderNumber = "order-123";
        var result = mockMvcTester
                .get()
                .uri("/admin/orders/{orderNumber}", orderNumber)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/order-details")
                .model()
                .containsKeys("order", "statuses")
                .satisfies(model -> {
                    var order = model.get("order");
                    assertThat(order).isInstanceOf(OrderDto.class);

                    OrderDto orderDto = (OrderDto) order;
                    assertThat(orderDto.orderNumber()).isEqualTo(orderNumber);

                    var statuses = model.get("statuses");
                    assertThat(statuses).isEqualTo(OrderStatus.values());
                });
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldReturn500WhenOrderNotFound() {
        String nonExistentOrderNumber = "non-existent-order";
        var result = mockMvcTester
                .get()
                .uri("/admin/orders/{orderNumber}", nonExistentOrderNumber)
                .exchange();

        assertThat(result).hasStatus(HttpStatus.OK).hasViewName("error/404");
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldUpdateOrderStatus_Success() {
        String orderNumber = "order-123";
        OrderStatus newStatus = OrderStatus.IN_PROCESS;
        String comments = "Order is being processed";

        var result = mockMvcTester
                .put()
                .uri("/admin/orders/{orderNumber}/status", orderNumber)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("status", newStatus.name())
                .param("comments", comments)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasRedirectedUrl("/admin/orders/" + orderNumber)
                .flash()
                .containsKey("message");
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldHandleException_WhenUpdateFails() {
        // Using a non-existent order number to trigger an exception
        String nonExistentOrderNumber = "non-existent-order";
        OrderStatus newStatus = OrderStatus.IN_PROCESS;

        var result = mockMvcTester
                .put()
                .uri("/admin/orders/{orderNumber}/status", nonExistentOrderNumber)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("status", newStatus.name())
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.FOUND)
                .hasRedirectedUrl("/admin/orders/" + nonExistentOrderNumber)
                .flash()
                .containsKey("error");
    }
}
