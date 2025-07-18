package com.sivalabs.bookstore.orders.events;

import static com.sivalabs.bookstore.orders.core.OrderStatus.CANCELLED;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.OrdersTestData;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(
        mode = DIRECT_DEPENDENCIES,
        extraIncludes = {"config"},
        webEnvironment = RANDOM_PORT)
@Sql("/test-books-data.sql")
class OrderCancelledEventHandlerTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(OrderCancelledEventHandlerTest.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Test
    void shouldHandleOrderCancelledEvent() {
        var request = OrdersTestData.getCreateOrderRequest();
        OrderSummary orderSummary = orderService.createOrder(request);

        log.info("Cancelling OrderNumber:{}", orderSummary.orderNumber());
        orderEventPublisher.send(new OrderCancelledEvent(
                orderSummary.orderNumber(), "testing", Set.of(), request.customer(), request.deliveryAddress()));

        await().atMost(30, SECONDS).untilAsserted(() -> {
            Optional<OrderDto> orderOptional = orderService.findOrderByOrderNumber(orderSummary.orderNumber());
            assertThat(orderOptional).isPresent();
            assertThat(orderOptional.get().status()).isEqualTo(CANCELLED);
        });
    }
}
