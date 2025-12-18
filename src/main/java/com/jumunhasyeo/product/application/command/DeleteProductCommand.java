package com.jumunhasyeo.product.application.command;

import java.util.UUID;

public record DeleteProductCommand(
        UUID productId,
        UUID organizationId,
        Long userId,
        String role
) {
}
