package com.jumunhasyeo.product.application.command;

import com.jumunhasyeo.product.presentation.dto.req.ProductSearchCondition;
import org.springframework.data.domain.Pageable;

public record SearchProductCommand(
        ProductSearchCondition condition,
        Pageable pageable
) {
}
