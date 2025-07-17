package com.sivalabs.bookstore.cart;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
class CartControllerAdvice {

    @ModelAttribute("cartItemCount")
    public int cartItemCount(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        return cart == null ? 0 : cart.getItemCount();
    }
}
