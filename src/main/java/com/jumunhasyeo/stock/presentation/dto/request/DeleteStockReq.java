package com.jumunhasyeo.stock.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "재고 삭제 요청")
public record DeleteStockReq(
        @NotNull(message = "stockId는 필수 입니다.")
        @Schema(description = "삭제할 재고의 id", example = "77777777-7777-7777-7777-777777777777", required = true)
        UUID stockId,
        @NotNull(message = "삭제자 ID는 필수 입니다.")
        @Schema(description = "삭제자", example = "3L", required = true)
        Long userId
) {
}
