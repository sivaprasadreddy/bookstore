package com.sivalabs.bookstore.orders;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.models.CreateOrderCmd;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import java.math.BigDecimal;
import java.util.Set;

public class OrdersTestData {

    public static CreateOrderCmd getCreateOrderRequest() {
        Customer customer = new Customer("Siva", "siva@gmail.com", "99999999999");
        Address address = new Address("addr line 1", "addr line 2", "Hyderabad", "Telangana", "500072", "India");
        Set<OrderItemDto> items = Set.of(
                new OrderItemDto("P100", "Book 1", new BigDecimal("34.0"), "https://images.bookstore.com/p100.png", 1));
        return new CreateOrderCmd(1L, items, customer, address);
    }
}
