package com.jumunhasyeo.visualization.dto;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HubDto {
    private String hubId;
    private String name;
    private String type;
    private Double latitude;
    private Double longitude;
    private String address;

    public static HubDto from(Hub hub) {
        return HubDto.builder()
                .hubId(hub.getHubId().toString())
                .name(hub.getName())
                .type(hub.getHubType().name())
                .latitude(hub.getCoordinate().getLatitude())
                .longitude(hub.getCoordinate().getLongitude())
                .address(hub.getAddress().getStreet())
                .build();
    }
}
