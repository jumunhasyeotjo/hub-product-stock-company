package com.jumunhasyeo.hub.presentation.docs;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "허브 검색 조회",
        description = "다양한 조건(이름, 주소, 상품ID)으로 허브를 검색합니다. 페이징을 지원합니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "허브 검색 성공(page)",
                content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
                responseCode = "199",
                description = "허브 검색 성공(content)",
                content = @Content(schema = @Schema(implementation = HubRes.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 (유효하지 않은 파라미터)",
                content = @Content(schema = @Schema(implementation = ApiRes.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류",
                content = @Content(schema = @Schema(implementation = ApiRes.class))
        )
})
public @interface ApiDocSearchHub {
}
