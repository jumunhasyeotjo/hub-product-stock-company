package com.jumunhasyeo.hub.hub.application;

import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubNameUpdatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubUpdatedEvent;

public interface HubEventPublisher {
    void publishEvent(HubCreatedEvent event);
    void publishEvent(HubDeletedEvent event);
    void publishEvent(HubNameUpdatedEvent event);
    void publishEvent(HubUpdatedEvent event);
}
