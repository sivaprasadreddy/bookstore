package com.sivalabs.bookstore.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
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
        if (items.stream().noneMatch(i -> i.getCode().equals(item.getCode()))) {
            items.add(item);
            return;
        }
        items.stream()
                .filter(i -> i.getCode().equals(item.getCode()))
                .findFirst()
                .ifPresent(i -> i.setQuantity(i.getQuantity() + item.getQuantity()));
    }

    public void removeItem(String code) {
        this.items.removeIf(item -> item.getCode().equals(code));
    }

    public void updateItemQuantity(String code, int quantity) {
        if (quantity <= 0) {
            removeItem(code);
            return;
        }
        this.items.stream()
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }

    public static class LineItem {
        private String code;
        private String name;
        private BigDecimal price;
        private int quantity;

        public LineItem() {}

        public LineItem(String code, String name, BigDecimal price, int quantity) {
            this.code = code;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
