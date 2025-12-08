package com.jumunhasyeo.product.presentation.docs;

import com.library.passport.entity.ApiRes;
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
        summary = "상품 수정",
        description = "상품 정보를 수정합니다. 본인의 업체 상품만 수정 가능합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "상품 수정 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "성공 예시",
                                value = """
                                {
                                    "code": null,
                                    "message": null,
                                    "data": {
                                        "productId": "110e8400-e29b-41d4-a716-446655440001",
                                        "companyId": "220e8400-e29b-41d4-a716-446655440002",
                                        "name": "로제 떡볶이",
                                        "price": 16000,
                                        "description": "부드러운 로제 소스 떡볶이"
                                    }
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "PRODUCT_VALID_FAIL", "message": "유효성 검증에 실패했습니다.", "data": null }
                        """))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "권한 없음 (타 업체의 상품)",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "FORBIDDEN", "message": "권한이 없습니다.", "data": null }
                        """))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "상품을 찾을 수 없음",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "PRODUCT_NOT_FOUND", "message": "상품을 찾을 수 없습니다.", "data": null }
                        """))
        )
})
public @interface ApiDocUpdateProduct {
}