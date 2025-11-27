package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.domain.event.HubDomainEvent;

public interface HubEventPublisher {
    void publishEvent(HubCreatedEvent event);
    void publishEvent(HubDeletedEvent event);
}
