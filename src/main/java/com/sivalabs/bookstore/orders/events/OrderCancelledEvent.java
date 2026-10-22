package com.sivalabs.bookstore.orders.events;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import java.util.Set;

public class OrderCancelledEvent {
    private String orderNumber;
    private String reason;
    private Set<OrderItemDto> items;
    private Customer customer;
    private Address deliveryAddress;

    public OrderCancelledEvent(
            String orderNumber, String reason, Set<OrderItemDto> items, Customer customer, Address deliveryAddress) {
        this.orderNumber = orderNumber;
        this.reason = reason;
        this.items = items;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public String toString() {
        return "OrderCancelledEvent{" + "orderNumber='"
                + orderNumber + '\'' + ", reason='"
                + reason + '\'' + ", items="
                + items + ", customer="
                + customer + ", deliveryAddress="
                + deliveryAddress + '}';
    }

    public String orderNumber() {
        return orderNumber;
    }

    public String reason() {
        return reason;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
}
