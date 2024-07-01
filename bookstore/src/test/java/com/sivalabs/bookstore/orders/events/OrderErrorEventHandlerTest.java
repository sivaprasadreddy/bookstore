package com.sivalabs.bookstore.orders.events;

import static com.sivalabs.bookstore.orders.domain.OrderStatus.ERROR;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.sivalabs.bookstore.common.AbstractIntegrationTest;
import com.sivalabs.bookstore.orders.OrdersTestData;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderResponse;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderErrorEvent;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-products-data.sql")
class OrderErrorEventHandlerTest extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Test
    void shouldHandleOrderErrorEvent() {
        var request = OrdersTestData.getCreateOrderRequest();
        CreateOrderResponse orderResponse = orderService.createOrder(request);

        orderEventPublisher.send(new OrderErrorEvent(
                orderResponse.orderId(), "testing", request.items(), request.customer(), request.deliveryAddress()));

        await().atMost(30, SECONDS).untilAsserted(() -> {
            Optional<OrderDTO> orderOptional = orderService.findOrderByOrderId(orderResponse.orderId());
            assertThat(orderOptional).isPresent();
            assertThat(orderOptional.get().status()).isEqualTo(ERROR);
        });
    }
}
