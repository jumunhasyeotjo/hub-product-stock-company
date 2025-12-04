package com.jumunhasyeo.hub.hubRoute.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoRouteResponse {

    @JsonProperty("trans_id")
    private String transId;

    @JsonProperty("routes")
    private List<Route> routes;

    @Getter
    @NoArgsConstructor
    public static class Route {
        @JsonProperty("result_code")
        private Integer resultCode;

        @JsonProperty("result_msg")
        private String resultMsg;

        @JsonProperty("summary")
        private Summary summary;
    }

    @Getter
    @NoArgsConstructor
    public static class Summary {
        @JsonProperty("origin")
        private Location origin;

        @JsonProperty("destination")
        private Location destination;

        @JsonProperty("distance")
        private Integer distance;  // 미터 단위

        @JsonProperty("duration")
        private Integer duration;  // 초 단위

        @JsonProperty("fare")
        private Fare fare;
    }

    @Getter
    @NoArgsConstructor
    public static class Location {
        @JsonProperty("name")
        private String name;

        @JsonProperty("x")
        private Double x;  // 경도 (longitude)

        @JsonProperty("y")
        private Double y;  // 위도 (latitude)
    }

    @Getter
    @NoArgsConstructor
    public static class Fare {
        @JsonProperty("taxi")
        private Integer taxi;

        @JsonProperty("toll")
        private Integer toll;
    }

    // 편의 메서드
    public BigDecimal getDistanceKm() {
        if (routes == null || routes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        Integer distanceMeters = routes.get(0).getSummary().getDistance();
        return BigDecimal.valueOf(distanceMeters)
                .divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP);
    }

    public Integer getDurationMinutes() {
        if (routes == null || routes.isEmpty()) {
            return 0;
        }
        Integer durationSeconds = routes.get(0).getSummary().getDuration();
        return (int) Math.ceil(durationSeconds / 60.0);
    }
}
