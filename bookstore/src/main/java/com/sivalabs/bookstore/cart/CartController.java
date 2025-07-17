package com.sivalabs.bookstore.cart;

import com.sivalabs.bookstore.catalog.CatalogAPI;
import com.sivalabs.bookstore.catalog.core.models.ProductDto;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRefreshView;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.FragmentsRendering;

@Controller
class CartController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CatalogAPI catalogAPI;

    CartController(CatalogAPI catalogAPI) {
        this.catalogAPI = catalogAPI;
    }

    @PostMapping("/add-to-cart")
    @HxRequest
    View addProductToCart(@RequestParam String code, HttpSession session) {
        log.info("Adding product code:{} to cart", code);
        Cart cart = CartUtil.getCart(session);
        ProductDto product = catalogAPI.findProductByCode(code).orElseThrow();
        cart.addItem(new Cart.LineItem(product.code(), product.name(), product.price(), 1));
        CartUtil.setCart(session, cart);
        boolean refresh = cart.isEmpty();
        if (refresh) {
            return new HtmxRefreshView();
        }
        return FragmentsRendering.with("partials/cart-item-count", Map.of("cartItemCount", cart.getItemCount()))
                .build();
    }

    @GetMapping({"/cart"})
    String showCart(Model model, HttpSession session) {
        Cart cart = CartUtil.getCart(session);
        model.addAttribute("cart", cart);
        OrderForm orderForm = OrderForm.empty();
        model.addAttribute("orderForm", orderForm);
        return "cart";
    }

    @HxRequest
    @PostMapping("/update-cart")
    View updateCart(@RequestParam String code, @RequestParam int quantity, HttpSession session) {
        log.info("Updating cart code:{}, quantity:{}", code, quantity);
        Cart cart = CartUtil.getCart(session);
        cart.updateItemQuantity(code, quantity);
        CartUtil.setCart(session, cart);
        boolean refresh = cart.getItems().isEmpty();
        if (refresh) {
            return new HtmxRefreshView();
        }
        return FragmentsRendering.with("partials/cart", Map.of("cart", cart)).build();
    }
}
