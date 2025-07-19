package com.sivalabs.bookstore.cart.core.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.bookstore.common.model.LineItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void shouldCreateEmptyCart() {
        // Given
        Cart cart = new Cart();

        // Then
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.getItemCount()).isZero();
        assertThat(cart.getTotalAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void shouldSetItems() {
        // Given
        Cart cart = new Cart();
        List<LineItem> items = new ArrayList<>();
        items.add(new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 2));

        // When
        cart.setItems(items);

        // Then
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems()).isEqualTo(items);
    }

    @Test
    void shouldCheckIfCartIsEmpty() {
        // Given
        Cart cart = new Cart();
        assertThat(cart.isEmpty()).isTrue();

        // When
        cart.addItem(new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1));

        // Then
        assertThat(cart.isEmpty()).isFalse();
    }

    @Test
    void shouldHandleNullItems() {
        // Given
        Cart cart = new Cart();
        cart.setItems(null);

        // Then
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.getItemCount()).isZero();
        assertThat(cart.getTotalAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateItemCount() {
        // Given
        Cart cart = new Cart();

        // When
        cart.addItem(new LineItem("1234567890", "Test Book 1", BigDecimal.valueOf(10.99), "image1.jpg", 2));
        cart.addItem(new LineItem("0987654321", "Test Book 2", BigDecimal.valueOf(15.99), "image2.jpg", 3));

        // Then
        assertThat(cart.getItemCount()).isEqualTo(5);
    }

    @Test
    void shouldCalculateTotalAmount() {
        // Given
        Cart cart = new Cart();

        // When
        cart.addItem(new LineItem("1234567890", "Test Book 1", BigDecimal.valueOf(10.00), "image1.jpg", 2));
        cart.addItem(new LineItem("0987654321", "Test Book 2", BigDecimal.valueOf(15.00), "image2.jpg", 3));

        // Then
        // 2 * 10.00 + 3 * 15.00 = 20.00 + 45.00 = 65.00
        assertThat(cart.getTotalAmount()).isEqualTo(BigDecimal.valueOf(65.00));
    }

    @Test
    void shouldAddNewItemToCart() {
        // Given
        Cart cart = new Cart();
        LineItem item = new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1);

        // When
        cart.addItem(item);

        // Then
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst().getIsbn()).isEqualTo("1234567890");
        assertThat(cart.getItems().getFirst().getQuantity()).isEqualTo(1);
    }

    @Test
    void shouldIncreaseQuantityWhenAddingExistingItem() {
        // Given
        Cart cart = new Cart();
        LineItem item1 = new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1);
        LineItem item2 = new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 2);

        // When
        cart.addItem(item1);
        cart.addItem(item2);

        // Then
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst().getIsbn()).isEqualTo("1234567890");
        assertThat(cart.getItems().getFirst().getQuantity()).isEqualTo(3);
    }

    @Test
    void shouldRemoveItemFromCart() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new LineItem("1234567890", "Test Book 1", BigDecimal.valueOf(10.99), "image1.jpg", 1));
        cart.addItem(new LineItem("0987654321", "Test Book 2", BigDecimal.valueOf(15.99), "image2.jpg", 2));

        // When
        cart.removeItem("1234567890");

        // Then
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst().getIsbn()).isEqualTo("0987654321");
    }

    @Test
    void shouldHandleRemovingNonExistentItem() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1));

        // When
        cart.removeItem("9999999999");

        // Then
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
    }

    @Test
    void shouldUpdateItemQuantity() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1));

        // When
        cart.updateItemQuantity("1234567890", 5);

        // Then
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst().getQuantity()).isEqualTo(5);
    }

    @Test
    void shouldRemoveItemWhenUpdatingQuantityToZero() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1));

        // When
        cart.updateItemQuantity("1234567890", 0);

        // Then
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void shouldRemoveItemWhenUpdatingQuantityToNegative() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1));

        // When
        cart.updateItemQuantity("1234567890", -1);

        // Then
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    void shouldHandleUpdatingNonExistentItem() {
        // Given
        Cart cart = new Cart();
        cart.addItem(new LineItem("1234567890", "Test Book", BigDecimal.valueOf(10.99), "image.jpg", 1));

        // When
        cart.updateItemQuantity("9999999999", 5);

        // Then
        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().getFirst().getIsbn()).isEqualTo("1234567890");
        assertThat(cart.getItems().getFirst().getQuantity()).isEqualTo(1);
    }
}
