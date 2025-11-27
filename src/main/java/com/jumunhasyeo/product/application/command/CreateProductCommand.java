package com.jumunhasyeo.product.application.command;

import java.util.UUID;

public record CreateProductCommand (
        String name,
        UUID organizationId,
        Integer price,
        String description,
        Long userId
){}
