package com.jumunhasyeo.stock.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "재고 생성 요청")
public record CreateStockReq(
        @NotNull(message = "hubId는 필수 입니다.")
        @Schema(description = "재고의 허브 id", example = "77777777-7777-7777-7777-777777777777", required = true)
        UUID hubId,
        @NotNull(message = "productId는 필수 입니다.")
        @Schema(description = "재고의 상품 id", example = "77777777-7777-7777-7777-777777777777", required = true)
        UUID productId,
        @NotNull(message = "재고 수량은 필수 입니다.")
        @Schema(description = "재고의 수량", example = "30", required = true)
        Integer quantity
) {

}
