package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.catalog.CatalogAPI;
import com.sivalabs.bookstore.catalog.core.models.BookDto;
import com.sivalabs.bookstore.orders.core.models.*;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import com.sivalabs.bookstore.orders.events.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.events.OrderEventPublisher;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CatalogAPI catalogAPI;
    private final OrderEventPublisher orderEventPublisher;

    OrderService(
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            CatalogAPI catalogAPI,
            OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.catalogAPI = catalogAPI;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public OrderSummary createOrder(CreateOrderCmd orderRequest) {
        validate(orderRequest);
        Order newOrder = orderMapper.convertToEntity(orderRequest);
        Order savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderNumber={}", savedOrder.getOrderNumber());
        return new OrderSummary(
                savedOrder.getId(), savedOrder.getOrderNumber(), savedOrder.getStatus(), savedOrder.getCreatedAt());
    }

    private void validate(CreateOrderCmd req) {
        req.items().forEach(item -> {
            BookDto product = catalogAPI
                    .findBookByIsbn(item.isbn())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Book isbn: " + item.isbn()));
            if (product.price().compareTo(item.price()) != 0) {
                throw new InvalidOrderException("Book price mismatch for isbn: " + item.isbn());
            }
        });
    }

    @Transactional(readOnly = true)
    public Optional<OrderDto> findOrderByOrderNumber(String orderNumber) {
        return this.orderRepository.findByOrderNumber(orderNumber).map(orderMapper::toDTO);
    }

    @Transactional
    public void updateOrderStatus(String orderNumber, OrderStatus status, String comments) {
        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        order.setStatus(status);
        order.setComments(comments);
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<OrderSummary> findUserOrders(Long userId) {
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return orderRepository.findUserOrders(userId, sort);
    }

    @Transactional
    public void processNewOrders() {
        List<Order> newOrders = orderRepository.findByStatus(OrderStatus.NEW);
        for (Order order : newOrders) {
            this.updateOrderStatus(order.getOrderNumber(), OrderStatus.IN_PROCESS, null);
            OrderCreatedEvent orderCreatedEvent = this.buildOrderCreatedEvent(order);
            orderEventPublisher.send(orderCreatedEvent);
            log.info("Published OrderCreatedEvent for orderNumber:{}", order.getOrderNumber());
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(
                order.getOrderNumber(), getOrderItems(order), order.getCustomer(), order.getDeliveryAddress());
    }

    private Set<OrderItemDto> getOrderItems(Order order) {
        return order.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getLineItem().getIsbn(),
                        item.getLineItem().getName(),
                        item.getLineItem().getPrice(),
                        item.getLineItem().getImageUrl(),
                        item.getLineItem().getQuantity()))
                .collect(Collectors.toSet());
    }
}
