package com.sivalabs.bookstore.orders.web;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import jakarta.validation.Valid;

record OrderForm(@Valid Customer customer, @Valid Address deliveryAddress) {

    public static OrderForm empty() {
        return new OrderForm(new Customer("", "", ""), new Address("", "", "", "", "", ""));
    }
}
