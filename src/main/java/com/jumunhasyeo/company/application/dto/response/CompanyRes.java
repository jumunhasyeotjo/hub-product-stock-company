package com.jumunhasyeo.company.application.dto.response;

import com.jumunhasyeo.company.domain.entity.Company;
import com.jumunhasyeo.company.domain.entity.CompanyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "업체 응답")
public record CompanyRes(
        @Schema(description = "업체 ID")
        UUID companyId,
        @Schema(description = "허브 ID")
        UUID hubId,
        @Schema(description = "업체명")
        String name,
        @Schema(description = "업체 유형")
        CompanyType companyType,
        @Schema(description = "주소")
        String address
) {
    public static CompanyRes from(Company company) {
        return new CompanyRes(
                company.getCompanyId(),
                company.getHubId(),
                company.getName(),
                company.getCompanyType(),
                company.getAddress()
        );
    }
}
