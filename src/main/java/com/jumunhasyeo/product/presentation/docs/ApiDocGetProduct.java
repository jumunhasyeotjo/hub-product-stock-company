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
        summary = "상품 단건 조회",
        description = "상품 ID로 상세 정보를 조회합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
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
                                        "name": "매콤 떡볶이",
                                        "price": 15000,
                                        "description": "맛있는 매운맛 떡볶이"
                                    }
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "상품 없음",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "PRODUCT_NOT_FOUND", "message": "상품을 찾을 수 없습니다.", "data": null }
                        """))
        )
})
public @interface ApiDocGetProduct {
}