package com.sivalabs.bookstore.orders.domain;

import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderItem;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.OrderItemDTO;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order convertToEntity(CreateOrderRequest orderRequest) {
        Order newOrder = new Order();
        newOrder.setOrderId(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomerName(orderRequest.getCustomer().name());
        newOrder.setCustomerEmail(orderRequest.getCustomer().email());
        newOrder.setCustomerPhone(orderRequest.getCustomer().phone());
        newOrder.setDeliveryAddressLine1(orderRequest.getDeliveryAddress().addressLine1());
        newOrder.setDeliveryAddressLine2(orderRequest.getDeliveryAddress().addressLine2());
        newOrder.setDeliveryAddressCity(orderRequest.getDeliveryAddress().city());
        newOrder.setDeliveryAddressState(orderRequest.getDeliveryAddress().state());
        newOrder.setDeliveryAddressZipCode(orderRequest.getDeliveryAddress().zipCode());
        newOrder.setDeliveryAddressCountry(orderRequest.getDeliveryAddress().country());

        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemDTO item : orderRequest.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCode(item.getCode());
            orderItem.setName(item.getName());
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }
}
