package com.jumunhasyeo.hubRoute.application.dto.response;

import com.jumunhasyeo.hubRoute.domain.entity.HubRoute;

import java.util.UUID;

public record HubRouteRes(
        UUID routeId,
        UUID startHub,
        UUID endHub,
        int durationMinutes,
        int distanceKm  // 거리 (km)
) {
    public static HubRouteRes from(HubRoute route) {
        return new HubRouteRes(
                route.getRouteId(),
                route.getStartHub().getHubId(),
                route.getEndHub().getHubId(),
                route.getRouteWeight().getDurationMinutes(),
                (int) route.getRouteWeight().getDistanceKm().doubleValue()
        );
    }
}
