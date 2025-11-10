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
        summary = "허브 생성",
        description = "새로운 물류 허브를 생성합니다. 허브 이름, 주소, 좌표 정보가 필요합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "허브 생성 성공",
                content = @Content(schema = @Schema(implementation = HubRes.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터 (필수값 누락, 유효성 검증 실패, 중복 이름)",
                content = @Content(schema = @Schema(implementation = ApiRes.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiRes.class))
        )
})
public @interface ApiDocCreateHub {
}
