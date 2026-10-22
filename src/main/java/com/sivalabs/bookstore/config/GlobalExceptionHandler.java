package com.sivalabs.bookstore.config;

import com.sivalabs.bookstore.common.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    String handleGenericException(Exception e, Model model) {
        log.error("Unhandled exception", e);
        model.addAttribute("message", e.getMessage());
        return "error/500";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    String handle(ResourceNotFoundException e, Model model) {
        log.error("ResourceNotFoundException", e);
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }
}
