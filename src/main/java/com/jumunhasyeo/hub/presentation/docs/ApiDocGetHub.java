package com.jumunhasyeo.hub.presentation.docs;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
        description = "허브 ID를 통해 특정 허브의 상세 정보를 조회합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "허브 조회 성공",
                content = @Content(schema = @Schema(implementation = HubRes.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "허브를 찾을 수 없음",
                content = @Content(schema = @Schema(implementation = ApiRes.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (유효하지 않은 UUID 형식)",
                content = @Content(schema = @Schema(implementation = ApiRes.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiRes.class))
        )
})
public @interface ApiDocGetHub {
}
