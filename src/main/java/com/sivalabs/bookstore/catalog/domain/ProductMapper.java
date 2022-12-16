package com.sivalabs.bookstore.catalog.domain;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductModel toModel(Product product) {
        var discount = BigDecimal.ZERO;
        if (product.getDiscount() != null) {
            discount = product.getDiscount();
        }
        return ProductModel.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .discount(discount)
                .salePrice(product.getPrice().subtract(discount))
                .build();
    }
}
