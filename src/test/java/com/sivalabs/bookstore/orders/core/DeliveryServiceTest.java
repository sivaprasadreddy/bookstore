package com.sivalabs.bookstore.orders.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.core.models.OrderItemDto;
import com.sivalabs.bookstore.orders.core.models.OrderStatus;
import com.sivalabs.bookstore.orders.events.OrderCancelledEvent;
import com.sivalabs.bookstore.orders.events.OrderCreatedEvent;
import com.sivalabs.bookstore.orders.events.OrderDeliveredEvent;
import com.sivalabs.bookstore.orders.events.OrderErrorEvent;
import com.sivalabs.bookstore.orders.events.OrderEventPublisher;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @InjectMocks
    private DeliveryService deliveryService;

    private Order order;
    private OrderCreatedEvent orderCreatedEvent;
    private Set<OrderItemDto> orderItems;
    private Customer customer;
    private Address deliveryAddress;

    @BeforeEach
    void setUp() {
        // Initialize test data
        String orderNumber = "ORD-12345";

        // Create order
        order = new Order();
        order.setId(1L);
        order.setOrderNumber(orderNumber);
        order.setStatus(OrderStatus.NEW);

        // Create order items
        orderItems = new HashSet<>();
        orderItems.add(new OrderItemDto("ISBN-123", "Book 1", new BigDecimal("10.0"), "book1.jpg", 1));

        // Create customer
        customer = new Customer("John Doe", "john@example.com", "1234567890");

        // Create delivery address with country that can be delivered
        deliveryAddress = new Address("123 Main St", "Apt 4B", "New York", "NY", "10001", "USA");

        // Create OrderCreatedEvent
        orderCreatedEvent = new OrderCreatedEvent(orderNumber, orderItems, customer, deliveryAddress);

        // Mock repository to return the order when findByOrderNumber is called
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));
    }

    /**
     * Test successful delivery when the country is in the allowed list.
     * The order status should be updated to DELIVERED and an OrderDeliveredEvent should be published.
     */
    @Test
    void shouldDeliverOrderWhenCountryIsAllowed() {
        // Given
        // Setup is done in setUp() - using USA as the country which is in the allowed list

        // When
        deliveryService.process(orderCreatedEvent);

        // Then
        verify(orderRepository).findByOrderNumber(orderCreatedEvent.orderNumber());
        verify(orderRepository).save(order);
        verify(orderEventPublisher).send(any(OrderDeliveredEvent.class));

        // Verify order status is updated to DELIVERED
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    /**
     * Test case sensitivity handling in country names.
     * The delivery should succeed even if the country name has a different case (e.g., "usa" instead of "USA").
     */
    @Test
    void shouldDeliverOrderWhenCountryNameHasDifferentCase() {
        // Given
        // Create delivery address with country in lowercase
        Address addressWithLowercaseCountry = new Address(
                "123 Main St", "Apt 4B", "New York", "NY", "10001", "usa" // lowercase country name
                );

        OrderCreatedEvent eventWithLowercaseCountry = new OrderCreatedEvent(
                orderCreatedEvent.orderNumber(), orderItems, customer, addressWithLowercaseCountry);

        // When
        deliveryService.process(eventWithLowercaseCountry);

        // Then
        verify(orderRepository).findByOrderNumber(eventWithLowercaseCountry.orderNumber());
        verify(orderRepository).save(order);
        verify(orderEventPublisher).send(any(OrderDeliveredEvent.class));

        // Verify order status is updated to DELIVERED
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    /**
     * Test cancelled delivery when the country is not in the allowed list.
     * The order status should be updated to CANCELLED and an OrderCancelledEvent should be published.
     */
    @Test
    void shouldCancelOrderWhenCountryIsNotAllowed() {
        // Given
        // Create delivery address with country that cannot be delivered
        Address nonDeliverableAddress = new Address(
                "456 Some St", "Unit 7C", "Beijing", "Beijing", "100000", "China" // Not in the allowed countries list
                );

        OrderCreatedEvent nonDeliverableEvent =
                new OrderCreatedEvent(orderCreatedEvent.orderNumber(), orderItems, customer, nonDeliverableAddress);

        // When
        deliveryService.process(nonDeliverableEvent);

        // Then
        verify(orderRepository).findByOrderNumber(nonDeliverableEvent.orderNumber());
        verify(orderRepository).save(order);
        verify(orderEventPublisher).send(any(OrderCancelledEvent.class));

        // Verify order status is updated to CANCELLED
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    /**
     * Test error handling when an exception occurs during processing.
     * The order status should be updated to ERROR and an OrderErrorEvent should be published.
     */
    @Test
    void shouldHandleErrorWhenExceptionOccurs() {
        // Given
        // Create a test scenario where an exception occurs during canBeDelivered
        Address invalidAddress = new Address(
                "123 Main St",
                "Apt 4B",
                "New York",
                "NY",
                "10001",
                null // null country will cause NullPointerException in canBeDelivered
                );

        OrderCreatedEvent eventWithInvalidAddress =
                new OrderCreatedEvent(orderCreatedEvent.orderNumber(), orderItems, customer, invalidAddress);

        // When
        deliveryService.process(eventWithInvalidAddress);

        // Then
        // Verify that the orderEventPublisher.send method is called with an OrderErrorEvent
        verify(orderEventPublisher).send(any(OrderErrorEvent.class));

        // Verify that the repository methods are called
        verify(orderRepository).findByOrderNumber(eventWithInvalidAddress.orderNumber());
        verify(orderRepository).save(any(Order.class));
    }
}
