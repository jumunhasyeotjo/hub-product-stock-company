package com.jumunhasyeo.product.presentation.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "상품 삭제",
        description = "상품을 삭제(Soft Delete)합니다. 업체 관리자 또는 마스터 계정만 가능합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "삭제 성공",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "403",
                description = "권한 없음",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "FORBIDDEN", "message": "본인 업체의 상품만 삭제할 수 있습니다.", "data": null }
                        """))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "상품 없음",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                        { "code": "PRODUCT_NOT_FOUND", "message": "상품을 찾을 수 없습니다.", "data": null }
                        """))
        )
})
public @interface ApiDocDeleteProduct {
}