package com.jumunhasyeo.hubRoute.application.dto.response;

import java.math.BigDecimal;

public record RouteWeightRes(
        BigDecimal distanceKm,
        Integer durationMinutes
) {
}
