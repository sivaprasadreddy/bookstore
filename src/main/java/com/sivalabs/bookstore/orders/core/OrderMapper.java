package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.common.model.LineItem;
import com.sivalabs.bookstore.orders.core.models.CreateOrderCmd;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import com.sivalabs.bookstore.orders.core.models.OrderStatus;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
class OrderMapper {

    public Order convertToEntity(CreateOrderCmd orderRequest) {
        Order newOrder = new Order();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setUserId(orderRequest.userId());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(orderRequest.customer());
        newOrder.setDeliveryAddress(orderRequest.deliveryAddress());

        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemDto item : orderRequest.items()) {
            OrderItem orderItem = new OrderItem();
            LineItem lineItem = new LineItem();
            lineItem.setIsbn(item.isbn());
            lineItem.setName(item.name());
            lineItem.setPrice(item.price());
            lineItem.setImageUrl(item.imageUrl());
            lineItem.setQuantity(item.quantity());
            orderItem.setLineItem(lineItem);
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }

    public OrderDto toDTO(Order order) {
        Set<OrderItemDto> orderItemDtos = order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getLineItem().getIsbn(),
                        item.getLineItem().getName(),
                        item.getLineItem().getPrice(),
                        item.getLineItem().getImageUrl(),
                        item.getLineItem().getQuantity()))
                .collect(Collectors.toSet());

        return new OrderDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUserId(),
                orderItemDtos,
                order.getCustomer(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getComments());
    }
}
