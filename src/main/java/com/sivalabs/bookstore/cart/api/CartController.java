package com.sivalabs.bookstore.cart.api;

import com.sivalabs.bookstore.cart.domain.Cart;
import com.sivalabs.bookstore.cart.domain.CartItem;
import com.sivalabs.bookstore.cart.domain.CartNotFoundException;
import com.sivalabs.bookstore.cart.domain.CartRepository;
import com.sivalabs.bookstore.catalog.domain.Product;
import com.sivalabs.bookstore.catalog.domain.ProductNotFoundException;
import com.sivalabs.bookstore.catalog.domain.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final ProductService productService;
    private final CartRepository cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestParam(name = "cartId", required = false) String cartId) {
        if (!StringUtils.hasText(cartId)) {
            return ResponseEntity.ok(Cart.withNewId());
        }
        Cart cart = cartService.findById(cartId).orElseThrow(() -> new CartNotFoundException("Cart not found"));
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public Cart addToCart(@RequestParam(name = "cartId", required = false) String cartId,
                          @RequestBody CartItemRequestDTO cartItemRequest) {
        Cart cart;
        if (!StringUtils.hasText(cartId)) {
            cart = Cart.withNewId();
        } else {
            cart = cartService.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
        }
        log.info("Add productCode: {} to cart", cartItemRequest.getProductCode());
        Product product = productService.getProductByCode(cartItemRequest.getProductCode())
                .orElseThrow(() -> new ProductNotFoundException(cartItemRequest.getProductCode()));
        CartItem cartItem = new CartItem(product.getCode(), product.getName(),
                product.getDescription(), product.getPrice(),
                cartItemRequest.getQuantity() > 0 ? cartItemRequest.getQuantity() : 1);
        cart.addItem(cartItem);
        return cartService.save(cart);
    }

    @PutMapping
    public Cart updateCartItemQuantity(@RequestParam(name = "cartId") String cartId,
                                       @RequestBody CartItemRequestDTO cartItemRequest) {
        Cart cart = cartService.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
        log.info("Update quantity: {} for productCode:{} quantity in cart: {}", cartItemRequest.getQuantity(),
                cartItemRequest.getProductCode(), cartId);
        Product product = productService.getProductByCode(cartItemRequest.getProductCode())
                .orElseThrow(() -> new ProductNotFoundException(cartItemRequest.getProductCode()));
        if (cartItemRequest.getQuantity() <= 0) {
            cart.removeItem(product.getCode());
        } else {
            cart.updateItemQuantity(product.getCode(), cartItemRequest.getQuantity());
        }
        return cartService.save(cart);
    }

    @DeleteMapping(value = "/items/{productCode}")
    public Cart removeCartItem(@RequestParam(name = "cartId") String cartId,
                               @PathVariable("productCode") String productCode) {
        Cart cart = cartService.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
        log.info("Remove cart line item productCode: {}", productCode);
        cart.removeItem(productCode);
        return cartService.save(cart);
    }

    @DeleteMapping
    public void removeCart(@RequestParam(name = "cartId") String cartId) {
        cartService.deleteById(cartId);
    }
}
