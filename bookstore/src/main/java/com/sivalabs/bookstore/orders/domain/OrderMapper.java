package com.sivalabs.bookstore.orders.domain;

import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
class OrderMapper {

    public Order convertToEntity(CreateOrderRequest orderRequest) {
        Order newOrder = new Order();
        newOrder.setOrderId(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(orderRequest.customer());
        newOrder.setDeliveryAddress(orderRequest.deliveryAddress());

        Set<OrderItem> orderItems = new HashSet<>();
        for (com.sivalabs.bookstore.orders.domain.model.OrderItem item : orderRequest.items()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }

    public OrderDTO toDTO(Order order) {
        Set<com.sivalabs.bookstore.orders.domain.model.OrderItem> orderItems = order.getItems().stream()
                .map(item -> new com.sivalabs.bookstore.orders.domain.model.OrderItem(
                        item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());

        return new OrderDTO(
                order.getId(),
                order.getOrderId(),
                orderItems,
                order.getCustomer(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getComments());
    }
}
