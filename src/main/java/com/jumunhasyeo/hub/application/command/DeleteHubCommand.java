package com.jumunhasyeo.hub.application.command;

import java.util.UUID;

public record DeleteHubCommand(
        UUID hubId,
        Long userId
) {
}
