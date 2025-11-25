package com.jumunhasyeo.stock.application.command;

import java.util.UUID;

public record IncreaseStockCommand(
        UUID stockId,
        int amount
) {
}