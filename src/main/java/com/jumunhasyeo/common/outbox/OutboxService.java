package com.jumunhasyeo.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubNameUpdatedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteCreatedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void save(HubNameUpdatedEvent event) {
        try {
            Outbox outbox = Outbox.of("HubNameUpdatedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubDeletedEvent event) {
        try {
            Outbox outbox = Outbox.of("HubDeletedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubCreatedEvent event) {
        try {
            Outbox outbox = Outbox.of("hubCreatedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubRouteCreatedEvent event) {
        try {
            Outbox outbox = Outbox.of("HubRouteCreatedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubRouteDeletedEvent event) {
        try {
            Outbox outbox = Outbox.of("HubRouteDeletedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void markAsProcessed(String eventKey) {
        Outbox outbox = outboxRepository.findByEventKey(eventKey);
        outbox.markProcessed();
    }
}
