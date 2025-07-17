package com.sivalabs.bookstore.orders.web;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.core.OrderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config"},
        webEnvironment = RANDOM_PORT)
@Sql({"/test-books-data.sql", "/test-orders-data.sql"})
class OrderControllerTests extends AbstractIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Nested
    class CreateOrderApiTests {
        @Test
        void shouldCreateOrderSuccessfully() {}

        @Test
        void shouldReturnBadRequestWhenMandatoryDataIsMissing() {}
    }

    @Nested
    class GetOrderApiTests {
        String orderId = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {}

        @Test
        void shouldReturnNotFoundWhenOrderIdNotExist() {}
    }
}
