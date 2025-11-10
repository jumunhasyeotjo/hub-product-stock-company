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
        summary = "허브 삭제 (소프트 삭제)",
        description = "허브를 논리적으로 삭제합니다. 실제 데이터는 삭제되지 않고 deletedAt, deletedBy 필드만 업데이트됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "허브 삭제 성공",
                content = @Content(
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                value = """
                                {
                                  "code": null,
                                  "message": null,
                                  "data": {
                                    "id": "550e8400-e29b-41d4-a716-446655440000"
                                  }
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "잘못된 요청 데이터 (필수값 누락, 유효성 검증 실패)",
                content = @Content(
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                value = """
                                {
                                  "code": "VALIDATION_FAILED",
                                  "message": "입력값 검증에 실패했습니다. {hubId=허브 id는 필수입니다}"
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "허브를 찾을 수 없음",
                content = @Content(
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                value = """
                                {
                                  "code": "EN001",
                                  "message": "조회에 실패했습니다."
                                }
                                """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "서버 내부 오류",
                content = @Content(
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                value = """
                                {
                                  "code": "E001",
                                  "message": "서버 에러가 발생했습니다."
                                }
                                """
                        )
                )
        )
})
public @interface ApiDocDeleteHub {
}