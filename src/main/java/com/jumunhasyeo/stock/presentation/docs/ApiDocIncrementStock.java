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
        summary = "허브 재고 증가",
        description = "특정 상품의 허브 재고를 증가시킵니다. 입고 처리 시 사용됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "재고 증가 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "재고 증가 성공",
                                value = """
                    {
                        "status": "SUCCESS",
                        "data": {
                            "stockId": "550e8400-e29b-41d4-a716-446655440000",
                            "productId": "660e8400-e29b-41d4-a716-446655440001",
                            "hubId": "770e8400-e29b-41d4-a716-446655440002",
                            "quantity": 150
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
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청",
                content = @Content(mediaType = "application/json")
        )
})
public @interface ApiDocIncrementStock {
}