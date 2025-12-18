package com.jumunhasyeo.stock.application.command;

import java.util.UUID;

public record DeleteStockCommand(
        UUID stockId,
        Long userId
) {
}
