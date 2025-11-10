package com.jumunhasyeo.hub.application.dto.response;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.UUID;

@Schema(description = "허브 응답")
@Builder
public record HubRes(

        @Schema(description = "허브 ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "허브 이름", example = "서울특별시 센터")
        String name,

        @Schema(description = "허브 주소", example = "서울특별시 송파구 송파대로 55")
        String address,

        @Schema(description = "위도", example = "37.4783091")
        Double latitude,

        @Schema(description = "경도", example = "127.1230678")
        Double longitude
) implements Serializable {

    public static HubRes from(Hub hub) {
        Address addressVo = hub.getAddress();
        Coordinate coordinate = addressVo.getCoordinate();
        return new HubRes(
                hub.getHubId(),
                hub.getName(),
                addressVo.getStreet(),
                coordinate.getLatitude(),
                coordinate.getLongitude()
        );
    }
}