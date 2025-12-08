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
        summary = "상품 생성",
        description = "업체 관리자(COMPANY_MANAGER)가 새로운 상품을 등록합니다. 업체 ID는 토큰에서 추출하며, 상품명은 중복될 수 없습니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "상품 생성 성공",
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
                responseCode = "400",
                description = "유효성 검증 실패",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "PRODUCT_VALID_FAIL", "message": "유효성 검증에 실패했습니다.", "data": null }
                        """))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "업체 정보 없음",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "COMPANY_NOT_FOUND", "message": "업체가 존재하지 않습니다.", "data": null }
                        """))
        ),
        @ApiResponse(
                responseCode = "409",
                description = "이미 존재하는 상품명",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "NAME_ALREADY_EXISTS", "message": "이미 존재하는 상품명입니다.", "data": null }
                        """))
        )
})
public @interface ApiDocCreateProduct {
}