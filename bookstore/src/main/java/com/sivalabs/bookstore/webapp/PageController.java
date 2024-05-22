package com.sivalabs.bookstore.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class PageController {

    @GetMapping("/")
    String home() {
        return "home";
    }

    @GetMapping("/cart")
    String cart() {
        return "cart";
    }

    @GetMapping("/order/{orderId}")
    String showOrderDetails(@PathVariable String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order-details";
    }

    @GetMapping("/orders")
    String showOrderAllOrders() {
        return "orders";
    }
}
