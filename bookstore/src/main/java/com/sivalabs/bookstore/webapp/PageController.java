package com.sivalabs.bookstore.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class PageController {

    @GetMapping
    String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    String showProductsPage(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("pageNo", page);
        return "products";
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
