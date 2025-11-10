package com.jumunhasyeo.hub.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class HubDomainEvent {
    private final LocalDateTime occurredAt;

    protected HubDomainEvent() {
        this.occurredAt = LocalDateTime.now();
    }
}
