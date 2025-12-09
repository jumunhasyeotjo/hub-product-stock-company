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
        summary = "상품 검색 및 목록 조회",
        description = "조건(업체ID, 이름, 가격 범위)에 맞는 상품 목록을 페이징하여 조회합니다."
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
                                        "content": [
                                            {
                                                "productId": "110e8400-e29b-41d4-a716-446655440001",
                                                "companyId": { "companyId": "220e8400-e29b-41d4-a716-446655440002" },
                                                "name": "매콤 떡볶이",
                                                "price": 15000,
                                                "description": "설명"
                                            }
                                        ],
                                        "pageable": {
                                            "pageNumber": 0,
                                            "pageSize": 10,
                                            "sort": { "empty": true, "sorted": false, "unsorted": true },
                                            "offset": 0,
                                            "paged": true,
                                            "unpaged": false
                                        },
                                        "totalElements": 1,
                                        "totalPages": 1,
                                        "last": true,
                                        "size": 10,
                                        "number": 0,
                                        "sort": { "empty": true, "sorted": false, "unsorted": true },
                                        "numberOfElements": 1,
                                        "first": true,
                                        "empty": false
                                    }
                                }
                                """
                        )
                )
        )
})
public @interface ApiDocSearchProduct {
}