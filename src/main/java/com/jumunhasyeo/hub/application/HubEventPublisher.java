package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.hub.domain.event.HubDomainEvent;

public interface HubEventPublisher {
    void publishEvent(HubDomainEvent event);
}
