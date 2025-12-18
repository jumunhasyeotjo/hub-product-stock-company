package com.jumunhasyeo.visualization.dto;

import com.jumunhasyeo.hub.hubRoute.domain.entity.HubRoute;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteDto {
    private String fromHubId;
    private String toHubId;
    private String fromHubName;
    private String toHubName;
    private Double distance;
    private Integer duration;

    public static RouteDto from(HubRoute route) {
        return RouteDto.builder()
                .fromHubId(route.getStartHub().getHubId().toString())
                .toHubId(route.getEndHub().getHubId().toString())
                .fromHubName(route.getStartHub().getName())
                .toHubName(route.getEndHub().getName())
                .distance(route.getRouteWeight().getDistanceKm().doubleValue())
                .duration(route.getRouteWeight().getDurationMinutes())
                .build();
    }
}
