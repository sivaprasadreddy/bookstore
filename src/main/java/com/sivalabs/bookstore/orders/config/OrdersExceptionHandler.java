package com.sivalabs.bookstore.orders.config;

import com.sivalabs.bookstore.orders.core.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class OrdersExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(OrdersExceptionHandler.class);

    @ExceptionHandler(OrderNotFoundException.class)
    String handle(OrderNotFoundException e) {
        log.error("OrderNotFoundException", e);
        return "error/404";
    }
}
