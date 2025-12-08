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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void save(HubNameUpdatedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubNameUpdatedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubDeletedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubDeletedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubCreatedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("hubCreatedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubRouteCreatedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubRouteCreatedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serialize event payload");
        }
    }

    @Transactional
    public void save(HubRouteDeletedEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.of("HubRouteDeletedEvent", objectMapper.writeValueAsString(event), event.getEventKey());
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
    public void outboxProcess(OutboxEvent event) {
        try {
            if (!event.canRetry()) {
                event.markFailed("Max retry count exceeded");
                return;
            }
            kafkaTemplate.send(event.getEventName(), event.getPayload()).get(); // 동기 대기
            event.publishSuccess();
        } catch (Exception e) {
            event.publishFail(e.getMessage());
            outboxRepository.save(event);
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
