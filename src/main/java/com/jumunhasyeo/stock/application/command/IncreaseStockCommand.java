package com.jumunhasyeo.stock.application.command;

import java.util.UUID;

public record IncreaseStockCommand(
        UUID productId,
        int amount
) {
}