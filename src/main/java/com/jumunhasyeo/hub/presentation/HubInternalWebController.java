package com.jumunhasyeo.hub.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.hub.application.HubService;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.presentation.docs.ApiDocExistHub;
import com.jumunhasyeo.hub.presentation.docs.ApiDocGetHub;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Internal-Hub", description = "허브 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/hubs")
public class HubInternalWebController {
    private final HubService hubService;

    //허브 단건 조회
    @ApiDocGetHub
    @GetMapping("/{hubId}")
    public ResponseEntity<ApiRes<HubRes>> getById(
            @Parameter(description = "조회할 허브의 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable(name = "hubId") UUID hubId
    ) {
        HubRes hubRes = hubService.getById(hubId);
        return ResponseEntity.ok(ApiRes.success(hubRes));
    }

    //허브 전체 조회
    @GetMapping
    public ResponseEntity<ApiRes<List<HubRes>>> getAll() {
        List<HubRes> hubRes = hubService.getAll();
        return ResponseEntity.ok(ApiRes.success(hubRes));
    }

    @ApiDocExistHub
    @GetMapping("/{hubId}/exist")
    public ResponseEntity<ApiRes<Map<String, Boolean>>> exist(
            @Parameter(description = "조회할 허브의 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable(name = "hubId") UUID hubId
    ) {
        boolean result = hubService.existById(hubId);
        return ResponseEntity.ok(ApiRes.success(Map.of("exist", result)));
    }
}
