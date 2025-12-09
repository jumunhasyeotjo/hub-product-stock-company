package com.jumunhasyeo.stock.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderCancelEvent implements OrderCompensationEvent {
    private final UUID orderId;
    private final LocalDateTime occurredAt;

    @Override
    public String getKey() {
        return orderId.toString();
    }
}
