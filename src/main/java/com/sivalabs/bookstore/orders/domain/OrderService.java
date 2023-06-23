package com.sivalabs.bookstore.orders.domain;

import com.sivalabs.bookstore.orders.domain.entity.Order;
import com.sivalabs.bookstore.orders.domain.entity.OrderStatus;
import com.sivalabs.bookstore.orders.domain.model.CreateOrderRequest;
import com.sivalabs.bookstore.orders.domain.model.OrderConfirmationDTO;
import com.sivalabs.bookstore.orders.domain.model.OrderDTO;
import com.sivalabs.bookstore.orders.domain.model.Payment;
import com.sivalabs.bookstore.payment.domain.PaymentRequest;
import com.sivalabs.bookstore.payment.domain.PaymentResponse;
import com.sivalabs.bookstore.payment.domain.PaymentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, PaymentService paymentService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.orderMapper = orderMapper;
    }

    public OrderConfirmationDTO createOrder(CreateOrderRequest orderRequest) {
        Order newOrder = orderMapper.convertToEntity(orderRequest);

        Payment payment = orderRequest.getPayment();
        PaymentRequest paymentRequest = new PaymentRequest(
                payment.getCardNumber(), payment.getCvv(),
                payment.getExpiryMonth(), payment.getExpiryYear());
        PaymentResponse paymentResponse = paymentService.validate(paymentRequest);
        if (paymentResponse.status() != PaymentResponse.PaymentStatus.ACCEPTED) {
            newOrder.setStatus(OrderStatus.PAYMENT_REJECTED);
            newOrder.setComments("Payment rejected");
        }
        Order savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderId=" + savedOrder.getOrderId());
        return new OrderConfirmationDTO(savedOrder.getOrderId(), savedOrder.getStatus());
    }

    public void cancelOrder(String orderId) {
        log.info("Cancel order with orderId: {}", orderId);
        Order order =
                this.orderRepository.findByOrderId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderCancellationException(order.getOrderId(), "Order is already delivered");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public Optional<OrderDTO> findOrderByOrderId(String orderId) {
        return this.orderRepository.findByOrderId(orderId).map(OrderDTO::new);
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
}
