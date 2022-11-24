package com.sivalabs.bookstore.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShopController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @GetMapping("/order/{orderId}")
    public String showOrderConfirmation(@PathVariable String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "orderConfirmation";
    }
}
