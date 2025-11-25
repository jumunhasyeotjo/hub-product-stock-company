package com.jumunhasyeo.hubRoute.application.service;

import com.jumunhasyeo.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hubRoute.application.dto.response.RouteWeightRes;

public interface RouteWeightApiService {
    RouteWeightRes getRouteInfo(Coordinate start, Coordinate end);
}
