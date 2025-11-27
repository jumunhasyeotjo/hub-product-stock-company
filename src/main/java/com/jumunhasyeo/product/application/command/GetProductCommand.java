package com.jumunhasyeo.product.application.command;

import java.util.UUID;

public record GetProductCommand(
        UUID productId
) {
}
