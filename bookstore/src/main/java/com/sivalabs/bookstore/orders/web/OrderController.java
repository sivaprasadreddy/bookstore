package com.sivalabs.bookstore.orders.web;

import com.sivalabs.bookstore.cart.Cart;
import com.sivalabs.bookstore.cart.CartUtil;
import com.sivalabs.bookstore.cart.OrderForm;
import com.sivalabs.bookstore.orders.core.OrderNotFoundException;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.models.CreateOrderRequest;
import com.sivalabs.bookstore.orders.core.models.CreateOrderResponse;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderItem;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import com.sivalabs.bookstore.users.SecurityService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping("")
    String createOrder(@Valid OrderForm orderForm, HttpSession session) {
        Long userId = securityService.getLoginUserId().orElseThrow();
        Cart cart = CartUtil.getCart(session);
        Set<OrderItem> orderItems = cart.getItems().stream()
                .map(li -> new OrderItem(li.getIsbn(), li.getName(), li.getPrice(), li.getQuantity()))
                .collect(Collectors.toSet());

        CreateOrderRequest request = getCreateOrderRequest(userId, orderForm, orderItems);
        CreateOrderResponse orderResponse = orderService.createOrder(request);
        CartUtil.clearCart(session);
        return "redirect:/orders/" + orderResponse.orderId();
    }

    private static CreateOrderRequest getCreateOrderRequest(
            Long userId, OrderForm orderForm, Set<OrderItem> orderItems) {
        return new CreateOrderRequest(userId, orderItems, orderForm.customer(), orderForm.deliveryAddress());
    }

    @GetMapping("")
    String getOrders(Model model) {
        Long userId = securityService.getLoginUserId().orElseThrow();
        List<OrderSummary> orders = orderService.findUserOrders(userId);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping(value = "/{orderId}")
    String showOrder(@PathVariable String orderId, Model model) {
        log.info("Fetching order by id: {}", orderId);
        Long userId = securityService.getLoginUserId().orElseThrow();
        OrderDto orderDTO =
                orderService.findOrderByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (userId.longValue() != orderDTO.userId().longValue()) {
            throw new OrderNotFoundException(orderId);
        }
        model.addAttribute("order", orderDTO);
        return "order-details";
    }
}
