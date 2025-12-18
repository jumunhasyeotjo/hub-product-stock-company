package com.jumunhasyeo.company.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "업체 삭제 요청")
public record DeleteCompanyReq(
        @Schema(description = "삭제할 업체 ID", required = true)
        @NotNull(message = "업체 ID는 필수입니다")
        UUID companyId,

        @Schema(description = "삭제 요청자 ID", required = true)
        @NotNull(message = "삭제 요청자 ID는 필수입니다")
        Long userId
) {
}
