package com.sivalabs.bookstore.orders.api;

import com.sivalabs.bookstore.orders.domain.OrderNotFoundException;
import com.sivalabs.bookstore.orders.domain.OrderService;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderResponse;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
class OrderController {
    private final OrderService orderService;

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
    OrderDTO getOrder(@PathVariable String orderId) {
        return orderService.findOrderByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
