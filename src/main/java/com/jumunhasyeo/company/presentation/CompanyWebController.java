package com.jumunhasyeo.company.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.company.application.CompanyService;
import com.jumunhasyeo.company.application.command.CreateCompanyCommand;
import com.jumunhasyeo.company.application.command.DeleteCompanyCommand;
import com.jumunhasyeo.company.application.command.UpdateCompanyCommand;
import com.jumunhasyeo.company.application.dto.response.CompanyRes;
import com.jumunhasyeo.company.presentation.dto.request.CreateCompanyReq;
import com.jumunhasyeo.company.presentation.dto.request.DeleteCompanyReq;
import com.jumunhasyeo.company.presentation.dto.request.UpdateCompanyReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Company", description = "업체 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyWebController {

    private final CompanyService companyService;

    @Operation(summary = "나의 업체 단건 조회")
    @GetMapping("/{companyId}")
    public ResponseEntity<ApiRes<CompanyRes>> getById(
            @Parameter(description = "조회할 업체 ID", required = true)
            @PathVariable UUID companyId
    ) {
        CompanyRes companyRes = companyService.getById(companyId);
        return ResponseEntity.ok(ApiRes.success(companyRes));
    }

    @Operation(summary = "업체 전체 조회")
    @GetMapping
    public ResponseEntity<ApiRes<List<CompanyRes>>> getAll() {
        List<CompanyRes> companyRes = companyService.getAll();
        return ResponseEntity.ok(ApiRes.success(companyRes));
    }

    @Operation(summary = "업체 생성")
    @PostMapping
    public ResponseEntity<ApiRes<CompanyRes>> create(
            @Parameter(description = "업체 생성 요청", required = true)
            @RequestBody @Valid CreateCompanyReq req
    ) {
        CreateCompanyCommand command = new CreateCompanyCommand(
                req.hubId(), req.name(), req.companyType(), req.address()
        );
        CompanyRes companyRes = companyService.create(command);
        return ResponseEntity.created(URI.create("/api/v1/companies/" + companyRes.companyId()))
                .body(ApiRes.success(companyRes));
    }

    @Operation(summary = "업체 수정")
    @PatchMapping
    public ResponseEntity<ApiRes<CompanyRes>> update(
            @Parameter(description = "업체 수정 요청", required = true)
            @RequestBody @Valid UpdateCompanyReq req
    ) {
        UpdateCompanyCommand command = new UpdateCompanyCommand(
                req.companyId(), req.hubId(), req.name(), req.companyType(), req.address()
        );
        CompanyRes companyRes = companyService.update(command);
        return ResponseEntity.ok(ApiRes.success(companyRes));
    }

    @Operation(summary = "업체 삭제")
    @DeleteMapping
    public ResponseEntity<ApiRes<Map<String, UUID>>> delete(
            @Parameter(description = "업체 삭제 요청", required = true)
            @RequestBody @Valid DeleteCompanyReq req
    ) {
        DeleteCompanyCommand command = new DeleteCompanyCommand(req.companyId(), req.userId());
        UUID deletedId = companyService.delete(command);
        return ResponseEntity.ok(ApiRes.success(Map.of("id", deletedId)));
    }

    @Operation(summary = "업체 존재 여부 검증", description = "주문 등록시 존재하는 업체인지 확인")
    @GetMapping("/{companyId}/exists")
    public ResponseEntity<ApiRes<Boolean>> exists(
            @Parameter(description = "검증할 업체 ID", required = true)
            @PathVariable UUID companyId
    ) {
        boolean exists = companyService.existsById(companyId);
        return ResponseEntity.ok(ApiRes.success(exists));
    }

    @Operation(summary = "업체 소속 허브 검증", description = "해당 허브가 해당 업체 소속인지 검증")
    @GetMapping("/{companyId}/hub/{hubId}/exist")
    public ResponseEntity<ApiRes<Boolean>> existToHub(
            @Parameter(description = "검증할 업체 ID", required = true)
            @PathVariable UUID companyId,
            @Parameter(description = "검증할 허브 ID", required = true)
            @PathVariable UUID hubId
    ) {
        boolean exist = companyService.existsByIdAndHubId(companyId, hubId);
        return ResponseEntity.ok(ApiRes.success(exist));
    }
}
