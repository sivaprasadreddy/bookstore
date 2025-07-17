package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.orders.core.models.CreateOrderRequest;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
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
        newOrder.setUserId(orderRequest.userId());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(orderRequest.customer());
        newOrder.setDeliveryAddress(orderRequest.deliveryAddress());

        Set<OrderItem> orderItems = new HashSet<>();
        for (com.sivalabs.bookstore.orders.core.models.OrderItem item : orderRequest.items()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setIsbn(item.isbn());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }

    public OrderDto toDTO(Order order) {
        Set<com.sivalabs.bookstore.orders.core.models.OrderItem> orderItems = order.getItems().stream()
                .map(item -> new com.sivalabs.bookstore.orders.core.models.OrderItem(
                        item.getIsbn(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());

        return new OrderDto(
                order.getId(),
                order.getOrderId(),
                order.getUserId(),
                orderItems,
                order.getCustomer(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getComments());
    }
}
