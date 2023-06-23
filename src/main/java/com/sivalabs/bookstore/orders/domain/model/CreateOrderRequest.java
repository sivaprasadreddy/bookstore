package com.sivalabs.bookstore.orders.domain.model;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public class CreateOrderRequest {

    @NotEmpty(message = "Items cannot be empty.")
    private Set<OrderItemDTO> items;

    @Valid
    private Customer customer;

    @Valid
    private Address deliveryAddress;

    @Valid
    private Payment payment;

    public Set<OrderItemDTO> getItems() {
        return this.items;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Address getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setItems(Set<OrderItemDTO> items) {
        this.items = items;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
