package com.jumunhasyeo.stock.application.dto.response;

import com.jumunhasyeo.stock.domain.entity.Stock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "재고 응답")
@Builder
public record StockRes(
        @Schema(description = "재고 ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID stockId,
        @Schema(description = "상품 ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID productId,
        @Schema(description = "허브 ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID hubId,
        @Schema(description = "재고 수", example = "100")
        Integer quantity
) {
        public static StockRes from(Stock stock) {
                return new StockRes(
                        stock.getStockId(),
                        stock.getProductId(),
                        stock.getHubId(),
                        stock.getQuantity()
                );
        }
}