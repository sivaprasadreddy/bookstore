package com.sivalabs.bookstore;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        String promotionServiceType,
        String promotionServiceUrl,
        String newOrdersTopic,
        String deliveredOrdersTopic,
        String cancelledOrdersTopic,
        String errorOrdersTopic) {}
