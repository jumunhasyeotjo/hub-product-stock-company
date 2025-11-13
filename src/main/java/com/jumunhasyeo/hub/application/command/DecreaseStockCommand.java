package com.jumunhasyeo.hub.application.command;

import java.util.UUID;

public record DecreaseStockCommand(
        UUID productId,
        int amount
) {
}