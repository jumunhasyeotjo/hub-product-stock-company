package com.jumunhasyeo.hub.hub.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublishEventRegistry {
    HUB_CREATED_EVENT("HubCreatedEvent"),
    HUB_DELETED_EVENT("HubDeletedEvent"),
    HUB_NAME_UPDATED_EVENT("HubNameUpdatedEvent");

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
