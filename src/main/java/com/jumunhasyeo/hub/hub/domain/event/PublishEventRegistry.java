package com.jumunhasyeo.hub.hub.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublishEventRegistry {
    HUB_CREATED_EVENT("HubCreatedEvent"),
    HUB_DELETED_EVENT("HubDeletedEvent"),
    HUB_NAME_UPDATED_EVNET("HubNameUpdatedEvent");

    private final String eventName;
}
