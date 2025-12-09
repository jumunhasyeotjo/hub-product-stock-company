package com.jumunhasyeo.hub.hubRoute.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublishEventRegistry {
    HUB_ROUTE_CREATED_EVENT("HubRouteCreatedEvent"),
    HUB_ROUTE_DELETED_EVENT("HubRouteDeletedEvent");

    private final String eventName;
}
