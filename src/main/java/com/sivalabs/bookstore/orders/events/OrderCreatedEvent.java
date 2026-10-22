package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import java.util.Objects;
import java.util.Set;

public class OrderCreatedEvent {
    private String orderNumber;
    private Set<OrderItemDto> items;
    private Customer customer;
    private Address deliveryAddress;

    public OrderCreatedEvent(String orderNumber, Set<OrderItemDto> items, Customer customer, Address deliveryAddress) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(Set<OrderItemDto> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String orderNumber() {
        return orderNumber;
    }

    public Set<OrderItemDto> items() {
        return items;
    }

    public Customer customer() {
        return customer;
    }

    public Address deliveryAddress() {
        return deliveryAddress;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OrderCreatedEvent) obj;
        return Objects.equals(this.orderNumber, that.orderNumber)
                && Objects.equals(this.items, that.items)
                && Objects.equals(this.customer, that.customer)
                && Objects.equals(this.deliveryAddress, that.deliveryAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, items, customer, deliveryAddress);
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent[" + "orderNumber="
                + orderNumber + ", " + "items="
                + items + ", " + "customer="
                + customer + ", " + "deliveryAddress="
                + deliveryAddress + ']';
    }
}
