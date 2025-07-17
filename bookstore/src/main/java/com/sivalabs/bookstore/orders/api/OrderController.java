package com.sivalabs.bookstore.orders.api;

import com.sivalabs.bookstore.orders.core.OrderNotFoundException;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.models.CreateOrderRequest;
import com.sivalabs.bookstore.orders.core.models.CreateOrderResponse;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        return orderService.findAllOrderSummaries();
    }

    @GetMapping(value = "/{orderId}")
    OrderDto getOrder(@PathVariable String orderId) {
        log.info("Fetching order by id: {}", orderId);
        return orderService.findOrderByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
