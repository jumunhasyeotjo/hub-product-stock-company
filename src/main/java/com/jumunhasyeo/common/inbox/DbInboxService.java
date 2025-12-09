package com.jumunhasyeo.common.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.stock.infrastructure.event.OrderCompensationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.jumunhasyeo.stock.infrastructure.event.ListenEventRegistry.ORDER_CANCEL_EVENT;

@Service
@RequiredArgsConstructor
public class DbInboxService {
    private final DbInboxRepository dbInboxRepository;
    private final ObjectMapper objectMapper;

    public void save(OrderCompensationEvent event) throws JsonProcessingException {
        InboxEvent inboxEvent = InboxEvent.builder()
                .eventKey(event.getKey())
                .eventName(ORDER_CANCEL_EVENT.getEventName())
                .payload(objectMapper.writeValueAsString(event))
                .status(InboxStatus.RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();
        dbInboxRepository.save(inboxEvent);
    }
}
