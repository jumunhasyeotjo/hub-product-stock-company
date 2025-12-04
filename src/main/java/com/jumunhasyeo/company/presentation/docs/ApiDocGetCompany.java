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
        summary = "업체 단건 조회",
        description = "업체 ID로 특정 업체 정보를 조회합니다. 내부 시스템 간 통신에서 업체 정보 확인 시 사용됩니다."
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "업체 조회 성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiRes.class),
                        examples = @ExampleObject(
                                name = "업체 조회 성공",
                                value = """
                    {
                        "code": null,
                        "message": null,
                        "data": {
                            "companyId": "550e8400-e29b-41d4-a716-446655440000",
                            "hubId": "660e8400-e29b-41d4-a716-446655440001",
                            "name": "주식회사 테스트물류",
                            "address": "서울특별시 강남구 테헤란로 123",
                            "companyType": "PRODUCER"
                        }
                    }
                    """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "업체를 찾을 수 없음",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "업체 없음",
                                value = """
                    {
                        "code": "COMPANY_NOT_FOUND",
                        "message": "해당 업체를 찾을 수 없습니다.",
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
                        "message": "유효하지 않은 업체 ID 형식입니다.",
                        "data": null
                    }
                    """
                        )
                )
        )
})
public @interface ApiDocGetCompany {
}
