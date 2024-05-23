package com.sivalabs.bookstore.orders.domain;

import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.Address;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderResponse;
import com.sivalabs.bookstore.orders.domain.model.Customer;
import com.sivalabs.bookstore.orders.domain.model.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderErrorEvent;
import com.sivalabs.bookstore.orders.domain.model.OrderItem;
import com.sivalabs.bookstore.orders.domain.model.OrderSummary;
import com.sivalabs.bookstore.orders.events.OrderEventPublisher;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventPublisher orderEventPublisher;

    public CreateOrderResponse createOrder(CreateOrderRequest orderRequest) {
        Order newOrder = orderMapper.convertToEntity(orderRequest);
        Order savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderId={}", savedOrder.getOrderId());
        return new CreateOrderResponse(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    public Optional<OrderDTO> findOrderByOrderId(String orderId) {
        return this.orderRepository.findByOrderId(orderId).map(OrderDTO::from);
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public void updateOrderStatus(String orderId, OrderStatus status, String comments) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(status);
        order.setComments(comments);
        orderRepository.save(order);
    }

    public List<OrderSummary> findAllOrderSummaries() {
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return orderRepository.findAllOrderSummaries(sort);
    }

    public void processNewOrders() {
        List<Order> newOrders = this.findOrdersByStatus(OrderStatus.NEW);
        for (Order order : newOrders) {
            this.updateOrderStatus(order.getOrderId(), OrderStatus.IN_PROCESS, null);
            OrderCreatedEvent orderCreatedEvent = this.buildOrderCreatedEvent(order);
            orderEventPublisher.send(orderCreatedEvent);
            log.info("Published OrderCreatedEvent for orderId:{}", order.getOrderId());
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(
                order.getOrderId(), getOrderItems(order), getCustomer(order), getDeliveryAddress(order));
    }

    private OrderErrorEvent buildOrderErrorEvent(Order order, String reason) {
        return new OrderErrorEvent(
                order.getOrderId(), reason, getOrderItems(order), getCustomer(order), getDeliveryAddress(order));
    }

    private Set<OrderItem> getOrderItems(Order order) {
        return order.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());
    }

    private Customer getCustomer(Order order) {
        return order.getCustomer();
    }

    private Address getDeliveryAddress(Order order) {
        return order.getDeliveryAddress();
    }
}
