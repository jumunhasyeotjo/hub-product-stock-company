package com.jumunhasyeo.product.presentation.docs;

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
        summary = "주문 상품 정보 조회 (Internal)",
        description = "내부 MSA 통신용: 주문 서비스에서 상품 ID 목록(List)을 받아, 주문 생성에 필요한 상품 정보(가격, 이름, 업체ID)를 일괄 조회합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "상품 목록 응답",
                                value = """
                                {
                                    "code": null,
                                    "message": null,
                                    "data": [
                                        {
                                            "productId": "110e8400-e29b-41d4-a716-446655440001",
                                            "companyId": "990e8400-e29b-41d4-a716-446655440099",
                                            "name": "매콤 떡볶이",
                                            "price": 15000
                                        },
                                        {
                                            "productId": "220e8400-e29b-41d4-a716-446655440002",
                                            "companyId": "990e8400-e29b-41d4-a716-446655440099",
                                            "name": "모둠 튀김",
                                            "price": 5000
                                        }
                                    ]
                                }
                                """
                        )
                )
        )
})
public @interface ApiDocInternalSearchOrderProduct {
}