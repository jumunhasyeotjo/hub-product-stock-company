package com.jumunhasyeo.hub.hub.application;

import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;

public interface HubEventPublisher {
    void publishEvent(HubCreatedEvent event);
    void publishEvent(HubDeletedEvent event);
}
