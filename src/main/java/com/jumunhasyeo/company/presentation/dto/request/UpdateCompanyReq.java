package com.jumunhasyeo.company.presentation.dto.request;

import com.jumunhasyeo.company.domain.entity.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "업체 수정 요청")
public record UpdateCompanyReq(
        @Schema(description = "업체 ID", required = true)
        @NotNull(message = "업체 ID는 필수입니다")
        UUID companyId,

        @Schema(description = "허브 ID", required = true)
        @NotNull(message = "허브 ID는 필수입니다")
        UUID hubId,

        @Schema(description = "업체명", required = true)
        @NotBlank(message = "업체명은 필수입니다")
        String name,

        @Schema(description = "업체 유형", required = true)
        @NotNull(message = "업체 유형은 필수입니다")
        CompanyType companyType,

        @Schema(description = "주소", required = true)
        @NotBlank(message = "주소는 필수입니다")
        String address
) {
}
