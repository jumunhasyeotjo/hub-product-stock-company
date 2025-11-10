package com.jumunhasyeo.hub.application.command;

public record CreateHubCommand(
        String name,
        String address,
        Double latitude,
        Double longitude
) {
}
