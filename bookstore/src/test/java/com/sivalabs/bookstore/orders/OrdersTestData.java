package com.sivalabs.bookstore.orders;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.models.CreateOrderRequest;
import com.sivalabs.bookstore.orders.core.models.OrderItem;
import java.math.BigDecimal;
import java.util.Set;

public class OrdersTestData {

    public static CreateOrderRequest getCreateOrderRequest() {
        Customer customer = new Customer("Siva", "siva@gmail.com", "99999999999");
        Address address = new Address("addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India");
        Set<OrderItem> items = Set.of(new OrderItem("P100", "Product 1", new BigDecimal("34.0"), 1));
        return new CreateOrderRequest(items, customer, address);
    }
}
