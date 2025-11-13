package com.jumunhasyeo.hub.application.command;

import java.util.UUID;

public record IncreaseStockCommand(
        UUID productId,
        int amount
) {
}