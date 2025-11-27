package com.jumunhasyeo.product.presentation.dto.req;

import java.util.UUID;

public record ProductSearchCondition (
        UUID companyId,
        String name,
        Integer maxPrice,
        Integer minPrice
){}
