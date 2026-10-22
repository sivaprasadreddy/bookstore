package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.config.PaymentProperties;
import com.sivalabs.bookstore.orders.core.models.PaymentResult;
import com.sivalabs.bookstore.orders.core.models.ProcessPaymentCmd;
import java.time.YearMonth;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentProperties paymentProperties;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    PaymentService(
            PaymentProperties paymentProperties, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.paymentProperties = paymentProperties;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentResult processPayment(String orderNumber, long amountInPaise, ProcessPaymentCmd command) {
        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        Payment payment = Payment.initiated(order, amountInPaise, "INR");

        String validationMessage = validate(command);
        if (validationMessage != null) {
            payment.completeWithFailure(validationMessage);
            paymentRepository.save(payment);
            return new PaymentResult(false, validationMessage);
        }

        payment.completeSuccessfully();
        paymentRepository.save(payment);
        return new PaymentResult(true, "Payment completed successfully.");
    }

    private String validate(ProcessPaymentCmd command) {
        if (YearMonth.of(command.expiryYear(), command.expiryMonth()).isBefore(YearMonth.now())) {
            return "This card has expired.";
        }
        Set<String> testCardNumbers = paymentProperties.testCardNumbers();
        if (testCardNumbers == null || !testCardNumbers.contains(normalizeCardNumber(command.cardNumber()))) {
            return "Card number is not an accepted test card.";
        }
        return null;
    }

    private String normalizeCardNumber(String cardNumber) {
        return cardNumber.replaceAll("[\\s-]", "");
    }
}
