package com.jumunhasyeo.stock.application.dto.response;

import com.jumunhasyeo.stock.domain.entity.Stock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
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
        Integer quantity,
        @Schema(description = "삭제 일자", example = "2026-01-01T12:00:00")
        LocalDateTime deletedAt,
        @Schema(description = "삭제자 ID", example = "2L")
        Long deletedBy
) {
    public static StockRes from(Stock stock) {
        return new StockRes(
                stock.getStockId(),
                stock.getProductId(),
                stock.getHubId(),
                stock.getQuantity(),
                stock.getDeletedAt(),
                stock.getDeletedBy()
        );
    }
}