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
        summary = "허브 전체 조회",
        description = "시스템에 등록된 모든 허브 정보를 조회합니다. 삭제되지 않은 허브만 조회됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "허브 전체 조회 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "허브 전체 조회 성공",
                                value = """
                    {
                        "code": null,
                        "message": null,
                        "data": [
                            {
                                "id": "550e8400-e29b-41d4-a716-446655440000",
                                "name": "서울특별시 센터",
                                "address": "서울특별시 송파구 송파대로 55",
                                "latitude": 37.4783091,
                                "longitude": 127.1230678
                            },
                            {
                                "id": "660e8400-e29b-41d4-a716-446655440001",
                                "name": "경기 북부 센터",
                                "address": "경기도 고양시 덕양구 권율대로 570",
                                "latitude": 37.6584,
                                "longitude": 126.8320
                            }
                        ]
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "서버 내부 오류",
                                value = """
                    {
                        "code": "INTERNAL_SERVER_ERROR",
                        "message": "서버 내부 오류가 발생했습니다.",
                        "data": null
                    }
                    """
                        )
                )
        )
})
public @interface ApiDocGetAllHubs {
}
