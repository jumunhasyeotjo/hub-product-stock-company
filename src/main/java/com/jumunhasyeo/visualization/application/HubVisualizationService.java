package com.jumunhasyeo.visualization.application;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.hubRoute.domain.entity.HubRoute;
import com.jumunhasyeo.hub.hubRoute.domain.repository.HubRouteRepository;
import com.jumunhasyeo.visualization.dto.HubDto;
import com.jumunhasyeo.visualization.dto.RouteDto;
import com.jumunhasyeo.visualization.dto.VisualizationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubVisualizationService {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;

    public VisualizationData getVisualizationData() {
        List<Hub> hubs = hubRepository.findAll();
        List<HubRoute> routes = hubRouteRepository.findAll();

        log.info("Total hubs from DB: {}", hubs.size());
        log.info("Total routes from DB: {}", routes.size());

        // 중복 경로 제거 (양방향 경로를 단방향으로)
        Map<String, RouteDto> uniqueRoutesMap = new HashMap<>();
        
        for (HubRoute route : routes) {
            String fromId = route.getStartHub().getHubId().toString();
            String toId = route.getEndHub().getHubId().toString();
            
            // 항상 작은 ID를 key의 앞에 배치하여 양방향을 구분
            String key = fromId.compareTo(toId) < 0 
                ? fromId + "-" + toId 
                : toId + "-" + fromId;
            
            // 이미 존재하지 않을 때만 추가
            if (!uniqueRoutesMap.containsKey(key)) {
                RouteDto routeDto = RouteDto.builder()
                        .fromHubId(fromId)
                        .toHubId(toId)
                        .distance(route.getRouteWeight().getDistanceKm().doubleValue())
                        .duration(route.getRouteWeight().getDurationMinutes())
                        .fromHubName(route.getStartHub().getName())
                        .toHubName(route.getEndHub().getName())
                        .build();
                uniqueRoutesMap.put(key, routeDto);
            }
        }

        List<RouteDto> uniqueRoutes = new ArrayList<>(uniqueRoutesMap.values());
        log.info("Unique routes after deduplication: {}", uniqueRoutes.size());

        List<HubDto> hubDtos = hubs.stream()
                .map(hub -> HubDto.builder()
                        .hubId(hub.getHubId().toString())
                        .name(hub.getName())
                        .type(hub.getHubType().name())
                        .latitude(hub.getCoordinate().getLatitude())
                        .longitude(hub.getCoordinate().getLongitude())
                        .address(hub.getAddress().getStreet())
                        .build())
                .collect(Collectors.toList());

        int centerCount = (int) hubDtos.stream()
                .filter(h -> "CENTER".equals(h.getType()))
                .count();

        return VisualizationData.builder()
                .hubs(hubDtos)
                .routes(uniqueRoutes)
                .totalHubs(hubDtos.size())
                .totalRoutes(uniqueRoutes.size())
                .centerHubCount(centerCount)
                .branchHubCount(hubDtos.size() - centerCount)
                .build();
    }
}
