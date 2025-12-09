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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    @Value("${spring.kafka.topics.hub}")
    private String hubTopic;
    private final OutboxRepository outboxRepository;
    private final OutboxDispatcher outboxDispatcher;
    private final ObjectMapper objectMapper;

    @Transactional
    public void save(HubNameUpdatedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubNameUpdatedEvent", objectMapper.writeValueAsString(event), event.getEventKey(), hubTopic);
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubDeletedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubDeletedEvent", objectMapper.writeValueAsString(event), event.getEventKey(), hubTopic);
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubCreatedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("hubCreatedEvent", objectMapper.writeValueAsString(event), event.getEventKey(), hubTopic);
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubRouteCreatedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubRouteCreatedEvent", objectMapper.writeValueAsString(event), event.getEventKey(), hubTopic);
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubRouteDeletedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubRouteDeletedEvent", objectMapper.writeValueAsString(event), event.getEventKey(), hubTopic);
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void markAsProcessed(String eventKey) {
        OutboxEvent outboxEvent = outboxRepository.findByEventKey(eventKey);
        outboxEvent.markProcessed();
    }

    @Transactional
    public OutboxEvent outboxProcess(OutboxEvent event) {
        try {
            if (!event.canRetry()) {
                event.markFailed("Max retry count exceeded");
                outboxRepository.save(event);
                return event;
            }

            outboxDispatcher.dispatch(event);
            event.publishSuccess();
            outboxRepository.save(event);
            return event;
        } catch (Exception e) {
            event.publishFail(e.getMessage());
            outboxRepository.save(event);
            return event;
        }
    }

    public int cleanUp(LocalDateTime cutoff) {
        return outboxRepository.deleteByStatusAndCreatedAtBefore(
                OutboxStatus.COMPLETE, cutoff
        );
    }

    public List<OutboxEvent> findTop100ByStatusOrderByIdAsc(OutboxStatus outboxStatus) {
        return outboxRepository.findTop100ByStatusOrderByIdAsc(outboxStatus);
    }
}