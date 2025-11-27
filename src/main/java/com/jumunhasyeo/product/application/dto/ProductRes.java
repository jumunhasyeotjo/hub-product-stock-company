package com.jumunhasyeo.product.application.dto;

import com.jumunhasyeo.product.domain.entity.Product;

import java.util.UUID;

public record ProductRes(
        UUID productId,
        UUID companyId,
        String name,
        Integer price,
        String description
) {
    public static ProductRes of(Product product) {
        return new ProductRes(
                product.getId(),
                product.getCompanyId().getCompanyId(),
                product.getName().getName(),
                product.getPrice().getPrice(),
                product.getDescription().getDescription()
        );
    }
}
