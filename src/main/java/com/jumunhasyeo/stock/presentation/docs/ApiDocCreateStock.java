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
        summary = "재고 생성",
        description = "새로운 재고를 생성합니다. 허브와 상품이 존재해야 하며, 상품당 하나의 재고만 생성 가능합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "재고 생성 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "재고 생성 성공",
                                value = """
                                {
                                    "code": null,
                                    "message": null,
                                    "data": {
                                        "stockId": "550e8400-e29b-41d4-a716-446655440000",
                                        "productId": "660e8400-e29b-41d4-a716-446655440001",
                                        "hubId": "770e8400-e29b-41d4-a716-446655440002",
                                        "quantity": 100,
                                        "deletedAt": null,
                                        "deletedBy": null
                                    }
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (필수값 누락, 유효성 검증 실패)",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "필수값 누락",
                                value = """
                                {
                                    "code": "VALIDATION_FAILED",
                                    "message": "유효성 검증에 실패했습니다. {hubId=hubId는 필수 입니다.}",
                                    "data": null
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "허브 또는 상품을 찾을 수 없음",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "허브 또는 상품 없음",
                                value = """
                                {
                                    "code": "NOT_FOUND",
                                    "message": "허브 또는 상품이 존재하지 않습니다.",
                                    "data": null
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "이미 재고가 존재함 (상품당 하나의 재고만 허용)",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "중복 재고",
                                value = """
                                {
                                    "code": "CONFLICT",
                                    "message": "해당 상품의 재고가 이미 존재합니다.",
                                    "data": null
                                }
                                """
                        )
                )
        )
})
public @interface ApiDocCreateStock {
}
