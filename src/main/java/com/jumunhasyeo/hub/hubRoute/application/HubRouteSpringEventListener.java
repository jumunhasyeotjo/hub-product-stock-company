package com.jumunhasyeo.hub.hubRoute.application;

import com.jumunhasyeo.common.outbox.OutboxService;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteCreatedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteDeletedEvent;
import com.jumunhasyeo.hub.hubRoute.infrastructure.event.KafkaHubRouteEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouteSpringEventListener {

    private final KafkaHubRouteEventPublisher kafkaHubRouteEventPublisher;
    private final OutboxService outboxService;

    /**
     * HubRoute 생성 outbox 저장
     * 트랜잭션 커밋 전 실행
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleHubRouteCreated(HubRouteCreatedEvent event) {
        log.info("sync HubRouteCreatedEvent received size: {} ", event.getRouteId());
        outboxService.save(event);
    }

    /**
     * HubRoute 삭제 outbox 저장
     * 트랜잭션 커밋 전 실행
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleHubRouteDeleted(HubRouteDeletedEvent event) {
        log.info("sync HubRouteDeletedEvent received size: {} ", event.getRouteId());
        outboxService.save(event);
    }

    /**
     * HubRoute 생성 outbox 저장
     * 트랜잭션 커밋 전 실행
     */
    @Async("eventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void asyncHandleHubRouteCreated(HubRouteCreatedEvent event) {
        log.info("async HubRouteCreatedEvent received size: {} ", event.getRouteId());
        var result = kafkaHubRouteEventPublisher.publish(event);
        result.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                log.error("Failed to publish HubRouteCreatedEventList to Kafka size: {}", event.getRouteId(), exception);
            } else {
                outboxService.markAsProcessed(event.getEventKey());
            }
        });
    }

    /**
     * HubRoute 삭제 outbox 저장
     * 트랜잭션 커밋 전 실행
     */
    @Async("eventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void asyncHandleHubRouteDeleted(HubRouteDeletedEvent event) {
        log.info("async HubRouteDeletedEvent received size: {} ", event.getRouteId());

        var result = kafkaHubRouteEventPublisher.publish(event);
        result.whenComplete((sendResult, exception) -> {
            if (exception != null) {
                log.error("Failed to publish HubRouteCreatedEventList to Kafka size: {}", event.getRouteId(), exception);
            } else {
                outboxService.markAsProcessed(event.getEventKey());
            }
        });

    }
}
