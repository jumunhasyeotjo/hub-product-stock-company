package com.jumunhasyeo.hub.hub.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class HubDomainEvent {
    private final LocalDateTime occurredAt;
    private final String eventKey;

    public HubDomainEvent() {
        eventKey = UUID.randomUUID().toString();
        this.occurredAt = LocalDateTime.now();
    }

    public String getEventKey() {
        return occurredAt+"-"+eventKey;
    }
}
