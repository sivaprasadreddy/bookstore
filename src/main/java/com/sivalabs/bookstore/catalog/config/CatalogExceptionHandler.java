package com.sivalabs.bookstore.catalog.config;

import com.sivalabs.bookstore.catalog.core.BookNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class CatalogExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CatalogExceptionHandler.class);

    @ExceptionHandler(BookNotFoundException.class)
    String handle(BookNotFoundException e) {
        log.error("BookNotFoundException", e);
        return "error/404";
    }
}
