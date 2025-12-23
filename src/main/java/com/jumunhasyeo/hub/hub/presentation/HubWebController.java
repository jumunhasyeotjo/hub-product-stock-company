package com.jumunhasyeo.hub.hub.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.hub.application.HubService;
import com.jumunhasyeo.hub.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.hub.presentation.docs.*;
import com.jumunhasyeo.hub.hub.presentation.dto.HubSearchCondition;
import com.jumunhasyeo.hub.hub.presentation.dto.request.CreateHubReq;
import com.jumunhasyeo.hub.hub.presentation.dto.request.DeleteHubReq;
import com.jumunhasyeo.hub.hub.presentation.dto.request.UpdateHubReq;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Hub", description = "허브 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hubs")
public class HubWebController {

    private final HubService hubService;

    //TODO: 테스트용 삭제, 허브 단건 조회
    @ApiDocGetHub
    @GetMapping("/{hubId}")
    public ResponseEntity<ApiRes<HubRes>> getById(
            @Parameter(description = "조회할 허브의 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable(name = "hubId") UUID hubId
    ) {
        HubRes hubRes = hubService.getById(hubId);
        return ResponseEntity.ok(ApiRes.success(hubRes));
    }

    //TODO: 테스트용 삭제 허브 전체 조회
    @ApiDocGetAllHubs
    @GetMapping
    public ResponseEntity<ApiRes<List<HubRes>>> getAll() {
        List<HubRes> hubRes = hubService.getAll();
        return ResponseEntity.ok(ApiRes.success(hubRes));
    }

    //허브 검색 조회
    @ApiDocSearchHub
    @GetMapping("/search")
    public ResponseEntity<ApiRes<Page<HubRes>>> search(
            @Parameter(description = "허브 검색 조건", required = false)
            @ModelAttribute HubSearchCondition condition,
            @Parameter(description = "페이징 정보 (page, size, sort)", required = false)
            @PageableDefault Pageable pageable
    ) {
        Page<HubRes> hubRes = hubService.search(condition, pageable);
        return ResponseEntity.ok(ApiRes.success(hubRes));
    }

    //허브 생성 (TODO:MASTER)
    @ApiDocCreateHub
    @PostMapping
    public ResponseEntity<ApiRes<HubRes>> create(
            @Parameter(description = "허브 생성 요청 정보", required = true)
            @RequestBody @Valid CreateHubReq req
    ) {
        if(!req.validate()){
            throw new BusinessException(ErrorCode.CONTROLLER_INVALID_REQUEST);
        }
        CreateHubCommand command = new CreateHubCommand(req.centerHubId(), req.name(), req.address(), req.latitude(), req.longitude(), req.hubType());
        HubRes hubRes = hubService.create(command);
        return ResponseEntity.created(URI.create("/api/v1/hubs/" + hubRes.id())).body(ApiRes.success(hubRes));
    }

    //허브 수정 (TODO:MASTER)
    @ApiDocUpdateHub
    @PatchMapping
    public ResponseEntity<ApiRes<HubRes>> update(
            @Parameter(description = "허브 수정 요청 정보", required = true)
            @RequestBody @Valid UpdateHubReq req
    ) {
        UpdateHubCommand command = new UpdateHubCommand(req.hubId(), req.name(), req.address(), req.latitude(), req.longitude());
        HubRes hubRes = hubService.update(command);
        return ResponseEntity.ok(ApiRes.success(hubRes));
    }

    //허브 삭제 (TODO:MASTER)
    @ApiDocDeleteHub
    @DeleteMapping
    public ResponseEntity<ApiRes<Map<String, UUID>>> delete(
            @Parameter(description = "허브 수정 요청 정보", required = true)
            @RequestBody @Valid DeleteHubReq req
    ) {
        DeleteHubCommand command = new DeleteHubCommand(req.hubId(), req.userId());
        UUID deletedHubId = hubService.delete(command);
        return ResponseEntity.ok(ApiRes.success(Map.of("id", deletedHubId)));
    }
}
