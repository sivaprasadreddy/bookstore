package com.sivalabs.bookstore.orders.domain;

import com.sivalabs.bookstore.ApplicationProperties;
import com.sivalabs.bookstore.events.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.api.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.OrderConfirmationDTO;
import com.sivalabs.bookstore.payment.domain.PaymentRequest;
import com.sivalabs.bookstore.payment.domain.PaymentResponse;
import com.sivalabs.bookstore.payment.domain.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ApplicationProperties properties;

    public OrderConfirmationDTO createOrder(CreateOrderRequest orderRequest) {
        Order newOrder = orderMapper.convertToEntity(orderRequest);
        Order savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order ID=" + savedOrder.getId() + ", ref_num=" + savedOrder.getOrderId());

        PaymentRequest paymentRequest = new PaymentRequest(
                orderRequest.getCardNumber(), orderRequest.getCvv(),
                orderRequest.getExpiryMonth(), orderRequest.getExpiryYear());
        PaymentResponse paymentResponse = paymentService.authorize(paymentRequest);
        if(paymentResponse.getStatus() != PaymentResponse.PaymentStatus.ACCEPTED) {
            savedOrder.setStatus(OrderStatus.ERROR);
            this.updateOrderStatus(savedOrder.getOrderId(), savedOrder.getStatus(), "Payment rejected");
        } else {
            kafkaTemplate.send(properties.newOrdersTopic(), new OrderCreatedEvent(savedOrder.getOrderId()));
            log.info("Published OrderCreatedEvent for orderId:{}", savedOrder.getOrderId());
        }
        return new OrderConfirmationDTO(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    public Optional<Order> findOrderByOrderId(String orderId) {
        return this.orderRepository.findByOrderId(orderId);
    }

    public void cancelOrder(String orderId) {
        log.info("Cancel order with OrderId: {}", orderId);
        Order order = findOrderByOrderId(orderId).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Order is already delivered");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public void updateOrderStatus(String orderId, OrderStatus status, String comments) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow();
        order.setStatus(status);
        order.setComments(comments);
        orderRepository.save(order);
    }

}
