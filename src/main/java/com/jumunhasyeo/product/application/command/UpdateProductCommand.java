package com.jumunhasyeo.product.application.command;

import java.util.UUID;

public record UpdateProductCommand(
        UUID productId,
        Long userId,
        String name,
        Integer price,
        String description
) {
}
