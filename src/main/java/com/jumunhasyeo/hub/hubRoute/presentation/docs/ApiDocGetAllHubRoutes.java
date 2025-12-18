package com.jumunhasyeo.hub.hubRoute.presentation.docs;

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
        summary = "허브 경로 전체 조회",
        description = "시스템에 등록된 모든 허브 간 경로 정보를 조회합니다. 각 경로는 시작 허브, 종료 허브, 예상 소요 시간, 거리 정보를 포함합니다. 배송 경로 최적화 및 배송 시간 예측에 사용됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "허브 경로 전체 조회 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "허브 경로 전체 조회 성공",
                                value = """
                    {
                        "code": null,
                        "message": null,
                        "data": [
                            {
                                "routeId": "550e8400-e29b-41d4-a716-446655440000",
                                "startHub": "660e8400-e29b-41d4-a716-446655440001",
                                "endHub": "770e8400-e29b-41d4-a716-446655440002",
                                "distanceKm": 25,
                                "durationMinutes": 45
                            },
                            {
                                "routeId": "880e8400-e29b-41d4-a716-446655440003",
                                "startHub": "770e8400-e29b-41d4-a716-446655440002",
                                "endHub": "990e8400-e29b-41d4-a716-446655440004",
                                "distanceKm": 38,
                                "durationMinutes": 62
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
public @interface ApiDocGetAllHubRoutes {
}
