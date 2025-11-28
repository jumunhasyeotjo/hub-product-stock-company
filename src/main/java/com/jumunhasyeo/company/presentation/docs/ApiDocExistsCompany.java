package com.jumunhasyeo.company.presentation.docs;

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
        summary = "업체 존재 여부 검증",
        description = "주문 등록 시 업체가 실제로 존재하는지 검증합니다. 주문 서비스에서 업체 유효성 확인 시 사용됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "업체 존재 여부 확인 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = {
                                @ExampleObject(
                                        name = "업체 존재함",
                                        value = """
                                {
                                    "code": null,
                                    "message": null,
                                    "data": true
                                }
                                """
                                ),
                                @ExampleObject(
                                        name = "업체 존재하지 않음",
                                        value = """
                                {
                                    "code": null,
                                    "message": null,
                                    "data": false
                                }
                                """
                                )
                        }
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (유효하지 않은 UUID 형식)",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "잘못된 UUID 형식",
                                value = """
                    {
                        "code": "INVALID_INPUT",
                        "message": "유효하지 않은 업체 ID 형식입니다.",
                        "data": null
                    }
                    """
                        )
                )
        )
})
public @interface ApiDocExistsCompany {
}
