package com.jumunhasyeo.stock.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderRolledBackEvent implements OrderCompensationEvent {
    private final String key;
    private final UUID orderId;
    private final LocalDateTime occurredAt;
}
