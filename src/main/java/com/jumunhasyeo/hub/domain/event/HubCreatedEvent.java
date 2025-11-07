package com.jumunhasyeo.hub.domain.event;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.vo.Address;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public final class HubCreatedEvent extends HubDomainEvent {
    private final UUID hubId;
    private final String name;
    private final Address address;

    public HubCreatedEvent(UUID hubId, String name, Address address) {
        this.hubId = hubId;
        this.name = name;
        this.address = address;
    }

    public static HubCreatedEvent of(Hub hub) {
        return new HubCreatedEvent(
                hub.getHubId(),
                hub.getName(),
                hub.getAddress()
        );
    }
}