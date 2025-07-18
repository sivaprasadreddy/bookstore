package com.sivalabs.bookstore.cart.web;

import com.sivalabs.bookstore.cart.core.models.CartUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
class CartControllerAdvice {

    @ModelAttribute("cartItemCount")
    public int cartItemCount(HttpSession session) {
        return CartUtil.getCart(session).getItemCount();
    }
}
