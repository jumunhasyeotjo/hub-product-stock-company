package com.jumunhasyeo.company.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.company.application.CompanyService;
import com.jumunhasyeo.company.application.dto.response.CompanyRes;
import com.jumunhasyeo.company.presentation.docs.ApiDocExistsCompany;
import com.jumunhasyeo.company.presentation.docs.ApiDocExistsCompanyToHub;
import com.jumunhasyeo.company.presentation.docs.ApiDocGetCompany;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Internal-Company", description = "내부용 업체 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/companies")
public class CompanyInternalWebController {
    private final CompanyService companyService;

    //업체 단건 조회
    @ApiDocGetCompany
    @GetMapping("/{companyId}")
    public ResponseEntity<ApiRes<CompanyRes>> getById(
            @Parameter(description = "조회할 업체 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID companyId
    ) {
        CompanyRes companyRes = companyService.getById(companyId);
        return ResponseEntity.ok(ApiRes.success(companyRes));
    }

    //업체 존재 여부 검증
    @ApiDocExistsCompany
    @GetMapping("/{companyId}/exists")
    public ResponseEntity<ApiRes<Boolean>> exists(
            @Parameter(description = "검증할 업체 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID companyId
    ) {
        boolean exists = companyService.existsById(companyId);
        return ResponseEntity.ok(ApiRes.success(exists));
    }

    //업체 소속 허브 검증
    @ApiDocExistsCompanyToHub
    @GetMapping("/{companyId}/hub/{hubId}/exists")
    public ResponseEntity<ApiRes<Boolean>> existToHub(
            @Parameter(description = "검증할 업체 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID companyId,
            @Parameter(description = "검증할 허브 ID", required = true, example = "660e8400-e29b-41d4-a716-446655440001")
            @PathVariable UUID hubId
    ) {
        boolean exist = companyService.existsByIdAndHubId(companyId, hubId);
        return ResponseEntity.ok(ApiRes.success(exist));
    }
}
