package com.sivalabs.bookstore.cart.core.models;

import com.sivalabs.bookstore.common.model.LineItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<LineItem> items = new ArrayList<>();

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public boolean isEmpty() {
        return this.items == null || this.items.isEmpty();
    }

    public int getItemCount() {
        if (this.isEmpty()) {
            return 0;
        }
        return items.stream().mapToInt(LineItem::getQuantity).sum();
    }

    public BigDecimal getTotalAmount() {
        if (this.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(LineItem item) {
        if (items.stream().noneMatch(i -> i.getIsbn().equals(item.getIsbn()))) {
            items.add(item);
            return;
        }
        items.stream()
                .filter(i -> i.getIsbn().equals(item.getIsbn()))
                .findFirst()
                .ifPresent(i -> i.setQuantity(i.getQuantity() + item.getQuantity()));
    }

    public void removeItem(String isbn) {
        this.items.removeIf(item -> item.getIsbn().equals(isbn));
    }

    public void updateItemQuantity(String isbn, int quantity) {
        if (quantity <= 0) {
            removeItem(isbn);
            return;
        }
        this.items.stream()
                .filter(item -> item.getIsbn().equals(isbn))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }
}
