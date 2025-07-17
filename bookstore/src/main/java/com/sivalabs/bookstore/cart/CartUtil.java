package com.sivalabs.bookstore.cart;

import jakarta.servlet.http.HttpSession;

public final class CartUtil {
    private CartUtil() {}

    private static final String CART_SESSION_KEY = "cart";

    public static Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
        }
        return cart;
    }

    public static void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public static void setCart(HttpSession session, Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }
}
