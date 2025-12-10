package com.jumunhasyeo.stock.application.command;

import java.util.UUID;

public record StoreStockCommand(
        UUID hubId,
        UUID productId,
        int amount
) {
}
