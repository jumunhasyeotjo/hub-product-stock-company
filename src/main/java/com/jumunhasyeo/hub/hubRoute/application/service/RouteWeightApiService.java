package com.jumunhasyeo.hub.hubRoute.application.service;

import com.jumunhasyeo.hub.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hub.hubRoute.application.dto.response.RouteWeightRes;

public interface RouteWeightApiService {
    RouteWeightRes getRouteInfo(Coordinate start, Coordinate end);
}
