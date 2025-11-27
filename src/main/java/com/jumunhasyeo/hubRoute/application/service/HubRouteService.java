package com.jumunhasyeo.hubRoute.application.service;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.entity.HubType;
import com.jumunhasyeo.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hubRoute.application.dto.response.HubRouteRes;
import com.jumunhasyeo.hubRoute.application.dto.response.RouteWeightRes;
import com.jumunhasyeo.hubRoute.application.command.BuildRouteCommand;
import com.jumunhasyeo.hubRoute.domain.entity.HubRoute;
import com.jumunhasyeo.hubRoute.domain.repository.HubRouteRepository;
import com.jumunhasyeo.hubRoute.domain.service.HubRouteDomainService;
import com.jumunhasyeo.hubRoute.domain.vo.RouteWeight;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubRouteService {
    private final HubRepository hubRepository;
    private final RouteWeightApiService routeWeightApi;
    private final HubRouteRepository hubRouteRepository;
    private final HubRouteDomainService hubRouteDomainService;

    /**
     * 새로운 Hub 생성 시 경로 자동 생성
     */
    @Transactional
    public void buildRoutesForNewHub(BuildRouteCommand command) {
        HubType type = command.type();
        if (type == HubType.CENTER) {
            buildForCenter(command);
        } else if (type == HubType.BRANCH) {
            buildForBranch(command);
        }
    }
    /**
     * 중앙 허브에 대한 경로 생성
     */
    private void buildForCenter(BuildRouteCommand command) {
        Hub newCenterHub = getHub(command.hubId());
        List<Hub> existingCenterHubs = hubRepository.findAllByHubType(HubType.CENTER);
        
        // Domain Service에 Route 생성 로직 위임
        Set<HubRoute> routes = hubRouteDomainService.buildRoutesForNewCenterHub(
            newCenterHub, 
            existingCenterHubs,
            this::calculateRouteWeight
        );
        
        hubRouteRepository.insertAllIgnore(routes);
    }

    /**
     * 지점 허브에 대한 경로 생성
     */
    private void buildForBranch(BuildRouteCommand command) {
        Hub branchHub = getHub(command.hubId());
        Hub centerHub = getHub(command.centerHubId());
        
        Set<HubRoute> routes = hubRouteDomainService.buildRoutesForNewBranchHub(
            branchHub,
            centerHub,
            this::calculateRouteWeight
        );
        
        hubRouteRepository.insertAllIgnore(routes);
    }

    /**
     * 경로 가중치(시간,거리) 계산
     */
    private RouteWeight calculateRouteWeight(Hub from, Hub to) {
        RouteWeightRes response = routeWeightApi.getRouteInfo(
            from.getCoordinate(), 
            to.getCoordinate()
        );
        return RouteWeight.of(response.distanceKm(), response.durationMinutes());
    }

    private Hub getHub(UUID hubId) {
        return hubRepository.findById(hubId)
            .orElseThrow(() -> new BusinessException(ErrorCode.HUB_NOT_FOUND));
    }

    /**
     * 허브 삭제 시 해당 허브와 연결된 모든 경로 소프트 삭제
     */
    @Transactional
    public void deleteRoutesForHub(UUID hubId, Long deletedBy) {
        Hub hub = getHub(hubId);
        
        // 해당 Hub가 시작점이거나 끝점인 모든 경로 조회
        List<HubRoute> routes = hubRouteRepository.findByStartHubOrEndHub(hub, hub);
        
        if (routes.isEmpty()) {
            log.info("No routes found for hub: {}", hub.getName());
            return;
        }
        
        // 모든 경로 소프트 삭제
        routes.forEach(route -> route.markDeleted(deletedBy));
        hubRouteRepository.saveAll(routes);
    }

    public List<HubRouteRes> getALLRoute() {
        return hubRouteRepository.findAll()
                .stream()
                .map(HubRouteRes::from)
                .collect(Collectors.toList());
    }
}
