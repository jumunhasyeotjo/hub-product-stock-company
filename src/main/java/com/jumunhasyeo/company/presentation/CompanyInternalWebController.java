package com.jumunhasyeo.company.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.company.application.CompanyService;
import com.jumunhasyeo.company.application.dto.response.CompanyRes;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "업체 단건 조회")
    @GetMapping("/{companyId}")
    public ResponseEntity<ApiRes<CompanyRes>> getById(
            @Parameter(description = "조회할 업체 ID", required = true)
            @PathVariable UUID companyId
    ) {
        CompanyRes companyRes = companyService.getById(companyId);
        return ResponseEntity.ok(ApiRes.success(companyRes));
    }
}

