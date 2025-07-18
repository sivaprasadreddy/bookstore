package com.sivalabs.bookstore.orders.web;

import com.sivalabs.bookstore.cart.CartUtil;
import com.sivalabs.bookstore.cart.core.models.Cart;
import com.sivalabs.bookstore.orders.core.OrderNotFoundException;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.models.CreateOrderCmd;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import com.sivalabs.bookstore.users.UsersAPI;
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
    private final UsersAPI usersAPI;

    OrderController(OrderService orderService, UsersAPI usersAPI) {
        this.orderService = orderService;
        this.usersAPI = usersAPI;
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
        Long userId = usersAPI.getLoginUserId();
        Set<OrderItemDto> orderItemDtos = cart.getItems().stream()
                .map(li ->
                        new OrderItemDto(li.getIsbn(), li.getName(), li.getPrice(), li.getImageUrl(), li.getQuantity()))
                .collect(Collectors.toSet());

        CreateOrderCmd request =
                new CreateOrderCmd(userId, orderItemDtos, orderForm.customer(), orderForm.deliveryAddress());
        OrderSummary orderSummary = orderService.createOrder(request);
        CartUtil.clearCart(session);
        return "redirect:/orders/" + orderSummary.orderNumber();
    }

    @GetMapping("")
    String getOrders(Model model) {
        Long userId = usersAPI.getLoginUserId();
        List<OrderSummary> orders = orderService.findUserOrders(userId);
        model.addAttribute("orders", orders);
        return "orders/orders";
    }

    @GetMapping(value = "/{orderNumber}")
    String showOrder(@PathVariable String orderNumber, Model model) {
        log.info("Fetching order by id: {}", orderNumber);
        Long userId = usersAPI.getLoginUserId();
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
