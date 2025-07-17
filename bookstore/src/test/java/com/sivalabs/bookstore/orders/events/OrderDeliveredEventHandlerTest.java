package com.sivalabs.bookstore.orders.events;

import static com.sivalabs.bookstore.orders.core.OrderStatus.DELIVERED;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.modulith.test.ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.OrdersTestData;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.models.CreateOrderResponse;
import com.sivalabs.bookstore.orders.core.models.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;

@ApplicationModuleTest(mode = DIRECT_DEPENDENCIES, webEnvironment = RANDOM_PORT)
@Sql("/test-products-data.sql")
class OrderDeliveredEventHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Test
    void shouldHandleOrderDeliveredEvent() {
        var request = OrdersTestData.getCreateOrderRequest();
        CreateOrderResponse order = orderService.createOrder(request);

        orderEventPublisher.send(
                new OrderDeliveredEvent(order.orderId(), Set.of(), request.customer(), request.deliveryAddress()));

        await().atMost(30, SECONDS).untilAsserted(() -> {
            Optional<OrderDto> orderOptional = orderService.findOrderByOrderId(order.orderId());
            assertThat(orderOptional).isPresent();
            assertThat(orderOptional.get().status()).isEqualTo(DELIVERED);
        });
    }
}
