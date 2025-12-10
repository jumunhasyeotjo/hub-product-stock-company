package com.jumunhasyeo.stock.infrastructure.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.jumunhasyeo.stock.infrastructure.event.ListenEventRegistry.*;

@Service
@RequiredArgsConstructor
public class OrderAclService {

    public String convert(String eventType) {
        return switch (eventType) {
            case "ORDER_ROLLEDBACK" -> ORDER_ROLLED_BACK_EVENT.getEventName();
            case "ORDER_CANCELLED" -> ORDER_CANCEL_EVENT.getEventName();
            case "ORDER_CREATED" -> ORDER_CREATED.getEventName();
            default -> throw new IllegalArgumentException();
        };
    }
}
