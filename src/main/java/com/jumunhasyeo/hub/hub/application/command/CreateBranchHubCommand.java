package com.jumunhasyeo.hub.hub.application.command;

import com.jumunhasyeo.hub.hub.domain.entity.HubType;

import java.util.UUID;

public record CreateBranchHubCommand(
        UUID centerHubId,
        String name,
        String address,
        Double latitude,
        Double longitude,
        HubType hubType
) {
}