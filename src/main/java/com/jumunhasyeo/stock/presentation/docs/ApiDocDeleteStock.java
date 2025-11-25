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
        summary = "재고 삭제 (논리적 삭제)",
        description = "재고 ID로 특정 재고를 논리적으로 삭제합니다. deleted_at, deleted_by 필드가 설정됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "재고 삭제 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "재고 삭제 성공",
                                value = """
                    {
                        "code": null,
                        "message": null,
                        "data": {
                            "stockId": "550e8400-e29b-41d4-a716-446655440000",
                            "productId": "660e8400-e29b-41d4-a716-446655440001",
                            "hubId": "770e8400-e29b-41d4-a716-446655440002",
                            "quantity": 100,
                            "deletedAt": "2025-11-24T15:30:00",
                            "deletedBy": 123
                        }
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "재고를 찾을 수 없음",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "재고 없음",
                                value = """
                    {
                        "code": "STOCK_NOT_FOUND",
                        "message": "해당 재고를 찾을 수 없습니다.",
                        "data": null
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (검증 실패)",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "필수 값 누락",
                                value = """
                    {
                        "code": "INVALID_INPUT",
                        "message": "재고 ID와 사용자 ID는 필수입니다.",
                        "data": null
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "권한 없음 (삭제 권한 없음)",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "권한 없음",
                                value = """
                    {
                        "code": "FORBIDDEN",
                        "message": "재고 삭제 권한이 없습니다.",
                        "data": null
                    }
                    """
                        )
                )
        )
})
public @interface ApiDocDeleteStock {
}
