package com.jumunhasyeo.hubRoute.application.command;

import com.jumunhasyeo.hub.domain.entity.HubType;
import com.jumunhasyeo.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.domain.vo.Address;

import java.util.UUID;

public record BuildRouteCommand(
        UUID centerHubId,
        UUID hubId,
        String name,
        Address address,
        HubType type
) {

    public static BuildRouteCommand from(HubCreatedEvent event) {
        return new BuildRouteCommand(
                event.getCenterHubId(),
                event.getHubId(),
                event.getName(),
                event.getAddress(),
                event.getType()
        );
    }
}
