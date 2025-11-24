package com.jumunhasyeo.hub.presentation.docs;

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
        summary = "허브 존재 여부 확인",
        description = "허브 ID로 해당 허브가 존재하는지 확인합니다. 다른 서비스에서 허브 유효성 검증 시 사용됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = {
                                @ExampleObject(
                                        name = "허브가 존재하는 경우",
                                        value = """
                                        {
                                            "code": null,
                                            "message": null,
                                            "data": {
                                                "exist": true
                                            }
                                        }
                                        """
                                ),
                                @ExampleObject(
                                        name = "허브가 존재하지 않는 경우",
                                        value = """
                                        {
                                            "code": null,
                                            "message": null,
                                            "data": {
                                                "exist": false
                                            }
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
                                value = """
                                {
                                    "code": "INVALID_INPUT",
                                    "message": "유효하지 않은 허브 ID 형식입니다.",
                                    "data": null
                                }
                                """
                        )
                )
        )
})
public @interface ApiDocExistHub {
}
