package com.sivalabs.bookstore.orders.web;

import com.sivalabs.bookstore.cart.core.models.Cart;
import com.sivalabs.bookstore.cart.core.models.CartUtil;
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
import org.springframework.validation.BindingResult;
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

    @GetMapping("/checkout")
    String checkout(Model model, HttpSession session) {
        Cart cart = CartUtil.getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        OrderForm orderForm = OrderForm.empty();
        model.addAttribute("orderForm", orderForm);
        return "orders/checkout";
    }

    @PostMapping("")
    String createOrder(
            @ModelAttribute("orderForm") @Valid OrderForm orderForm, BindingResult bindingResult, HttpSession session) {
        Cart cart = CartUtil.getCart(session);
        if (bindingResult.hasErrors()) {
            return "orders/checkout";
        }
        Long userId = securityService.getLoginUserId().orElseThrow();
        Set<OrderItem> orderItems = cart.getItems().stream()
                .map(li -> new OrderItem(li.getIsbn(), li.getName(), li.getPrice(), li.getQuantity()))
                .collect(Collectors.toSet());

        CreateOrderRequest request = getCreateOrderRequest(userId, orderForm, orderItems);
        CreateOrderResponse orderResponse = orderService.createOrder(request);
        CartUtil.clearCart(session);
        return "redirect:/orders/" + orderResponse.orderNumber();
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
        return "orders/orders";
    }

    @GetMapping(value = "/{orderNumber}")
    String showOrder(@PathVariable String orderNumber, Model model) {
        log.info("Fetching order by id: {}", orderNumber);
        Long userId = securityService.getLoginUserId().orElseThrow();
        OrderDto orderDTO = orderService
                .findOrderByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        if (userId.longValue() != orderDTO.userId().longValue()) {
            throw new OrderNotFoundException(orderNumber);
        }
        model.addAttribute("order", orderDTO);
        return "orders/order-details";
    }
}
