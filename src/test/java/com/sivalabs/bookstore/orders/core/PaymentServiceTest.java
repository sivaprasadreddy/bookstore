package com.sivalabs.bookstore.orders.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sivalabs.bookstore.config.PaymentProperties;
import com.sivalabs.bookstore.orders.core.models.PaymentStatus;
import com.sivalabs.bookstore.orders.core.models.ProcessPaymentCmd;
import java.time.YearMonth;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private static final String ORDER_NUMBER = "order-123";

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(
                new PaymentProperties(Set.of("4111111111111111")), orderRepository, paymentRepository);
        when(paymentRepository.save(org.mockito.ArgumentMatchers.any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldAcceptConfiguredTestCard() {
        Order order = new Order();
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));

        var result = paymentService.processPayment(ORDER_NUMBER, 1299, command("4111 1111 1111 1111", nextYear()));

        assertThat(result.successful()).isTrue();
        assertThat(result.message()).isEqualTo("Payment completed successfully.");
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        assertThat(paymentCaptor.getValue().getStatus()).isEqualTo(PaymentStatus.SUCCESS);
    }

    @Test
    void shouldRejectUnconfiguredCardAndRecordFailedPayment() {
        Order order = new Order();
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));

        var result = paymentService.processPayment(ORDER_NUMBER, 1299, command("4000000000000002", nextYear()));

        assertThat(result.successful()).isFalse();
        assertThat(result.message()).isEqualTo("Card number is not an accepted test card.");
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        assertThat(paymentCaptor.getValue().getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    void shouldRejectExpiredCard() {
        Order order = new Order();
        when(orderRepository.findByOrderNumber(ORDER_NUMBER)).thenReturn(Optional.of(order));

        var result = paymentService.processPayment(ORDER_NUMBER, 1299, command("4111111111111111", lastYear()));

        assertThat(result.successful()).isFalse();
        assertThat(result.message()).isEqualTo("This card has expired.");
    }

    private ProcessPaymentCmd command(String cardNumber, int expiryYear) {
        return new ProcessPaymentCmd(cardNumber, 12, expiryYear, "123");
    }

    private int nextYear() {
        return YearMonth.now().plusYears(1).getYear();
    }

    private int lastYear() {
        return YearMonth.now().minusYears(1).getYear();
    }
}
