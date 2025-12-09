package com.jumunhasyeo.stock.infrastructure.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.jumunhasyeo.stock.infrastructure.event.ListenEventRegistry.ORDER_CANCEL_EVENT;
import static com.jumunhasyeo.stock.infrastructure.event.ListenEventRegistry.ORDER_ROLLED_BACK_EVENT;

@Service
@RequiredArgsConstructor
public class OrderAclService {

    public String convert(String fullTypeName) {
        return switch (fullTypeName) {
            case "ORDER_ROLLEDBACK" -> ORDER_ROLLED_BACK_EVENT.getEventName();
            case "ORDER_CANCELLED" -> ORDER_CANCEL_EVENT.getEventName();
            default -> throw new IllegalArgumentException();
        };
    }
}
