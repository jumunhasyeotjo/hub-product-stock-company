package com.jumunhasyeo.stock.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ListenEventRegistry {

    ORDER_CANCEL_EVENT("OrderCancelEvent"),
    ORDER_CREATED("OrderCreatedEvent"),
    ORDER_ROLLED_BACK_EVENT("OrderRolledBackEvent");

    private final String eventName;
}
