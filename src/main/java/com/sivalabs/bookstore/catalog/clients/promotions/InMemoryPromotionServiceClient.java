package com.sivalabs.bookstore.catalog.clients.promotions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "app.promotion-service-type", havingValue = "in-memory")
@Slf4j
public class InMemoryPromotionServiceClient implements PromotionServiceClient {
    private static final List<Promotion> PROMOTIONS = List.of(
            new Promotion(1L, "P100", new BigDecimal("2.5")),
            new Promotion(2L, "P101", new BigDecimal("1.5"))
    );

    @Override
    public List<Promotion> getPromotions(List<String> productCodes) {
        log.info("Fetching promotions from in-memory service for productCodes: {}", productCodes);
        return PROMOTIONS.stream().filter(p -> productCodes.contains(p.getProductCode())).toList();
    }

    @Override
    public Optional<Promotion> getPromotion(String productCode) {
        return PROMOTIONS.stream().filter(promotion -> promotion.getProductCode().equals(productCode)).findFirst();
    }
}
