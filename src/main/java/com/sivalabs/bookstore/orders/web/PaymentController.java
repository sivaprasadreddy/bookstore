package com.sivalabs.bookstore.orders.web;

import com.sivalabs.bookstore.orders.core.OrderNotFoundException;
import com.sivalabs.bookstore.orders.core.OrderService;
import com.sivalabs.bookstore.orders.core.PaymentService;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.ProcessPaymentCmd;
import com.sivalabs.bookstore.users.UsersAPI;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class PaymentController {

    private static final String PAYMENT_VIEW = "orders/payment";

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final UsersAPI usersAPI;

    PaymentController(PaymentService paymentService, OrderService orderService, UsersAPI usersAPI) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.usersAPI = usersAPI;
    }

    @GetMapping("/orders/{orderNumber}/payment")
    String showPaymentPage(@PathVariable String orderNumber, Model model) {
        model.addAttribute("order", getUserOrder(orderNumber));
        model.addAttribute("paymentForm", PaymentForm.empty());
        return PAYMENT_VIEW;
    }

    @PostMapping("/orders/{orderNumber}/payment")
    String processPayment(
            @PathVariable String orderNumber,
            @Valid @ModelAttribute("paymentForm") PaymentForm paymentForm,
            BindingResult bindingResult,
            Model model) {
        OrderDto order = getUserOrder(orderNumber);
        model.addAttribute("order", order);
        if (bindingResult.hasErrors()) {
            return PAYMENT_VIEW;
        }

        long amountInPaise = order.getTotalAmount().movePointRight(2).longValueExact();
        var result = paymentService.processPayment(orderNumber, amountInPaise, paymentForm.toCommand());
        if (!result.successful()) {
            bindingResult.reject("payment.failed", result.message());
            return PAYMENT_VIEW;
        }
        return "redirect:/orders/" + orderNumber + "?payment=success";
    }

    private OrderDto getUserOrder(String orderNumber) {
        OrderDto order = orderService
                .findOrderByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        if (!order.userId().equals(usersAPI.getLoginUserId())) {
            throw new OrderNotFoundException(orderNumber);
        }
        return order;
    }

    record PaymentForm(
            @NotBlank(message = "Card number is required.") @Pattern(regexp = "[0-9 -]{13,23}", message = "Enter a valid card number.") String cardNumber,

            @NotNull(message = "Expiry month is required.") @Min(1) @Max(12) Integer expiryMonth,

            @NotNull(message = "Expiry year is required.") @Min(2000) @Max(9999) Integer expiryYear,

            @NotBlank(message = "CVV is required.") @Pattern(regexp = "[0-9]{3,4}", message = "Enter a valid CVV.") String cvv) {

        static PaymentForm empty() {
            return new PaymentForm(null, null, null, null);
        }

        ProcessPaymentCmd toCommand() {
            return new ProcessPaymentCmd(cardNumber, expiryMonth, expiryYear, cvv);
        }
    }
}
