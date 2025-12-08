package com.jumunhasyeo.hub.hub.application.command;

import com.jumunhasyeo.hub.hub.domain.entity.HubType;

import java.util.UUID;

public record CreateHubCommand(
        UUID centerHubId,
        String name,
        String address,
        Double latitude,
        Double longitude,
        HubType hubType
) {

    public static CreateHubCommand createBranch(
            UUID centerHubId,
            String name,
            String address,
            Double latitude,
            Double longitude,
            HubType hubType) {
        return new CreateHubCommand(
                centerHubId,
                name,
                address,
                latitude,
                longitude,
                hubType
        );
    }

    public static CreateHubCommand createCenter(
            String name,
            String address,
            Double latitude,
            Double longitude,
            HubType hubType) {
        return new CreateHubCommand(
                null,
                name,
                address,
                latitude,
                longitude,
                hubType
        );
    }
}
