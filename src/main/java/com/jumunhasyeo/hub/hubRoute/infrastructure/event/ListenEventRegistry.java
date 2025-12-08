package com.jumunhasyeo.hub.hubRoute.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ListenEventRegistry {

    HUB_CREATED_EVENT("HubCreatedEvent"),
    HUB_DELETED_EVENT("HubDeletedEvent"),
    HUB_NAME_UPDATE_EVENT("HubNameUpdateEvent");

    private final String eventName;
}
