package com.jumunhasyeo.hub.hub.infrastructure.event;

import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubNameUpdatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ListenEventRegistry {

    HUB_CREATED_EVENT(HubCreatedEvent.class.getSimpleName()),
    HUB_DELETED_EVENT(HubDeletedEvent.class.getSimpleName()),
    HUB_UPDATE_EVENT(HubUpdatedEvent.class.getSimpleName());

    private final String eventName;
}
