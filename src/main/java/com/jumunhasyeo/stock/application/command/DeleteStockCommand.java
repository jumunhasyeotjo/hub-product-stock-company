package com.jumunhasyeo.stock.application.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteStockCommand(
        UUID stockId,
        Long userId
) {
}
