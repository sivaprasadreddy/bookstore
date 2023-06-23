package com.sivalabs.bookstore.payment.domain;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final CreditCardRepository creditCardRepository;

    public PaymentService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public PaymentResponse validate(PaymentRequest request) {
        Optional<CreditCard> creditCardOptional = creditCardRepository.findByCardNumber(request.cardNumber());
        if (creditCardOptional.isEmpty()) {
            return new PaymentResponse(PaymentResponse.PaymentStatus.REJECTED);
        }
        CreditCard creditCard = creditCardOptional.get();
        if (creditCard.getCvv().equals(request.cvv())
                && creditCard.getExpiryMonth() == request.expiryMonth()
                && creditCard.getExpiryYear() == request.expiryYear()) {
            return new PaymentResponse(PaymentResponse.PaymentStatus.ACCEPTED);
        }
        return new PaymentResponse(PaymentResponse.PaymentStatus.REJECTED);
    }
}
