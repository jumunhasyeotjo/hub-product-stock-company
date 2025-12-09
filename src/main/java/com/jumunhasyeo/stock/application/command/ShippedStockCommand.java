package com.jumunhasyeo.stock.application.command;

import java.util.UUID;

public record ShippedStockCommand(
        UUID hubId,
        UUID productId,
        int amount
) {
}
