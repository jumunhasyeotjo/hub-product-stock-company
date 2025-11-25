package com.jumunhasyeo.stock.application.command;

import java.util.UUID;

public record CreateStockCommand(
        UUID hubId,
        UUID productId,
        Integer quantity
) { }
