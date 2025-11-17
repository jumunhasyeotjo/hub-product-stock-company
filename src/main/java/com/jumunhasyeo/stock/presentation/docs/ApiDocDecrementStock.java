package com.jumunhasyeo.stock.presentation.docs;
import com.jumunhasyeo.common.ApiRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "허브 재고 감소",
        description = "특정 상품의 허브 재고를 감소시킵니다. 출고 처리 또는 주문 시 사용됩니다. " +
                "재고가 부족한 경우 예외가 발생합니다. (Atomic 연산 보장)"
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "재고 감소 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "재고 감소 성공",
                                value = """
                    {
                        "status": "SUCCESS",
                        "data": {
                            "stockId": "550e8400-e29b-41d4-a716-446655440000",
                            "productId": "660e8400-e29b-41d4-a716-446655440001",
                            "hubId": "770e8400-e29b-41d4-a716-446655440002",
                            "quantity": 50
                        },
                        "error": null
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "상품을 찾을 수 없음",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                    {
                        "status": "ERROR",
                        "data": null,
                        "error": {
                            "code": "NOT_FOUND",
                            "message": "해당 상품의 재고를 찾을 수 없습니다."
                        }
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "재고 부족 또는 잘못된 요청",
                content = @Content(
                        mediaType = "application/json",
                        examples = {
                                @ExampleObject(
                                        name = "재고 부족",
                                        value = """
                        {
                            "status": "ERROR",
                            "data": null,
                            "error": {
                                "code": "STOCK_VALID",
                                "message": "재고가 부족합니다."
                            }
                        }
                        """
                                ),
                                @ExampleObject(
                                        name = "음수 수량",
                                        value = """
                        {
                            "status": "ERROR",
                            "data": null,
                            "error": {
                                "code": "BAD_REQUEST",
                                "message": "감소 수량은 0보다 커야 합니다."
                            }
                        }
                        """
                                )
                        }
                )
        )
})
public @interface ApiDocDecrementStock {
}
