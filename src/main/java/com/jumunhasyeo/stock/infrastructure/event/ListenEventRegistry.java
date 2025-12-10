package com.jumunhasyeo.stock.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ListenEventRegistry {

    ORDER_CANCEL_EVENT(OrderCancelEvent.class.getSimpleName()),
    ORDER_CREATED(OrderCreatedEvent.class.getSimpleName()),
    ORDER_ROLLED_BACK_EVENT(OrderRolledBackEvent.class.getSimpleName());

    private final String eventName;
}
