package com.jumunhasyeo.hub.domain.event;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.entity.HubType;
import com.jumunhasyeo.hub.domain.vo.Address;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class HubCreatedEvent extends HubDomainEvent {
    private final UUID centerHubId;
    private final UUID hubId;
    private final String name;
    private final Address address;
    private final HubType type;

    public HubCreatedEvent(UUID centerHubId, UUID hubId, String name, Address address, HubType type) {
        this.centerHubId = centerHubId;
        this.hubId = hubId;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public static HubCreatedEvent centerHub(Hub hub) {
        return new HubCreatedEvent(
                null,
                hub.getHubId(),
                hub.getName(),
                hub.getAddress(),
                hub.getHubType()
        );
    }

    public static HubCreatedEvent branchHub(Hub hub, UUID centerHubId) {
        return new HubCreatedEvent(
                centerHubId,
                hub.getHubId(),
                hub.getName(),
                hub.getAddress(),
                hub.getHubType()
        );
    }
}