package com.jumunhasyeo.visualization.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VisualizationData {
    private List<HubDto> hubs;
    private List<RouteDto> routes;
    private int totalHubs;
    private int totalRoutes;
    private int centerHubCount;
    private int branchHubCount;
}
