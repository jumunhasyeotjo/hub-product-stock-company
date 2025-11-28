package com.jumunhasyeo.hubRoute.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.hubRoute.application.dto.response.HubRouteRes;
import com.jumunhasyeo.hubRoute.application.service.HubRouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "HubRoute", description = "허브 경로 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/api/v1/hubs")
public class HubRouteInternalWebController {

    private final HubRouteService hubRouteService;

    //허브 경로 전체 조회
    @GetMapping("/routes")
    public ResponseEntity<ApiRes<List<HubRouteRes>>> getAll() {
        List<HubRouteRes> hubRouteRes = hubRouteService.getALLRoute();
        return ResponseEntity.ok(ApiRes.success(hubRouteRes));
    }
}
