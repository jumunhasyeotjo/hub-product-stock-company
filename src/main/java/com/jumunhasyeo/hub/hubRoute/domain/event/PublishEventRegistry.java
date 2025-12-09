package com.jumunhasyeo.hub.hubRoute.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublishEventRegistry {
    HUB_ROUTE_CREATED_EVENT("HubRouteCreatedEvent"),
    HUB_ROUTE_DELETED_EVENT("HubRouteDeletedEvent");

    private final String eventName;

    public static String of(String simpleName) {
        for (PublishEventRegistry registry : values()) {
            if (registry.getEventName().equals(simpleName)) {
                return registry.getEventName();
            }
        }
        throw new IllegalArgumentException("No matching event found for simple name: " + simpleName);
    }
}
