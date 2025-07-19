package com.sivalabs.bookstore.admin.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.core.models.OrderStats;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(
        webEnvironment = RANDOM_PORT,
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config", "users", "orders"})
@Sql({"/test-books-data.sql", "/test-orders-data.sql"})
class DashboardControllerTests extends AbstractIntegrationTest {

    @Test
    @WithUserDetails("admin@gmail.com")
    void shouldShowDashboard() {
        var result = mockMvcTester.get().uri("/admin").exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .hasViewName("admin/dashboard")
                .model()
                .containsKey("orderStats")
                .satisfies(model -> {
                    var orderStats = model.get("orderStats");
                    assertThat(orderStats).isInstanceOf(OrderStats.class);

                    OrderStats stats = (OrderStats) orderStats;
                    assertThat(stats.totalOrders()).isEqualTo(2);
                    assertThat(stats.deliveredOrders()).isGreaterThanOrEqualTo(0);
                    assertThat(stats.pendingOrders()).isGreaterThanOrEqualTo(0);
                    assertThat(stats.cancelledOrders()).isGreaterThanOrEqualTo(0);
                });
    }

    @Test
    void shouldRedirectToLoginWhenNotAuthenticated() {
        var result = mockMvcTester.get().uri("/admin").exchange();

        // When not authenticated, Spring Security redirects to the login page
        assertThat(result).hasStatus(HttpStatus.FOUND);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void shouldReturnForbiddenForNonAdminUser() {
        var result = mockMvcTester.get().uri("/admin").exchange();

        assertThat(result).hasStatus(HttpStatus.FORBIDDEN);
    }
}
