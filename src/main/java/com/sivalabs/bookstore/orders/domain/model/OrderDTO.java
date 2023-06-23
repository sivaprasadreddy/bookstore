package com.sivalabs.bookstore.orders.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {
    private Long id;
    private String orderId;
    private Set<OrderItemDTO> items;
    private Customer customer;
    private Address deliveryAddress;
    private OrderStatus status;
    private String comments;

    public OrderDTO(Order order) {
        this.setId(order.getId());
        this.setOrderId(order.getOrderId());
        this.setCustomer(new Customer(order.getCustomerName(), order.getCustomerEmail(), order.getCustomerPhone()));

        this.setDeliveryAddress(new Address(
                order.getDeliveryAddressLine1(),
                order.getDeliveryAddressLine2(),
                order.getDeliveryAddressCity(),
                order.getDeliveryAddressState(),
                order.getDeliveryAddressZipCode(),
                order.getDeliveryAddressCountry()));
        this.setStatus(order.getStatus());
        this.setComments(order.getComments());

        Set<OrderItemDTO> orderItemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setCode(item.getCode());
                    itemDTO.setName(item.getName());
                    itemDTO.setPrice(item.getPrice());
                    itemDTO.setQuantity(item.getQuantity());
                    return itemDTO;
                })
                .collect(Collectors.toSet());
        this.setItems(orderItemDTOs);
    }

    public OrderDTO() {}

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0.0");
        for (OrderItemDTO orderItem : items) {
            amount = amount.add(orderItem.getSubTotal());
        }
        return amount;
    }

    public Long getId() {
        return this.id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public Set<OrderItemDTO> getItems() {
        return this.items;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Address getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public String getComments() {
        return this.comments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
