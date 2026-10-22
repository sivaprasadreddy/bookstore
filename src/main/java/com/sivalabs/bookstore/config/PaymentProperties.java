package com.sivalabs.bookstore.config;

import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(Set<String> testCardNumbers) {}
