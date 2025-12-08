package com.jumunhasyeo.hub.hub.presentation.docs;

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
        summary = "허브 단건 조회",
        description = "허브 ID를 통해 특정 허브의 상세 정보를 조회합니다. 내부 시스템 간 통신에서 허브 정보 확인 시 사용됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "허브 조회 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "허브 조회 성공",
                                value = """
                    {
                        "code": null,
                        "message": null,
                        "data": {
                            "id": "550e8400-e29b-41d4-a716-446655440000",
                            "name": "서울특별시 센터",
                            "address": "서울특별시 송파구 송파대로 55",
                            "latitude": 37.4783091,
                            "longitude": 127.1230678
                        }
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "허브를 찾을 수 없음",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "허브 없음",
                                value = """
                    {
                        "code": "HUB_NOT_FOUND",
                        "message": "해당 허브를 찾을 수 없습니다.",
                        "data": null
                    }
                    """
                        )
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
                        "message": "유효하지 않은 허브 ID 형식입니다.",
                        "data": null
                    }
                    """
                        )
                )
        )
})
public @interface ApiDocGetHub {
}
