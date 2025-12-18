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
        summary = "상품 존재 여부 확인 (Internal)",
        description = "내부 MSA 통신용: 상품 ID를 받아 해당 상품이 존재하는지 `Boolean` 값으로 반환합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "확인 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "존재함",
                                value = """
                                {
                                    "code": null,
                                    "message": null,
                                    "data": true
                                }
                                """
                        )
                )
        )
})
public @interface ApiDocInternalExistProduct {
}