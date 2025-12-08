package com.jumunhasyeo.common.scheduler;

import com.jumunhasyeo.common.outbox.Outbox;
import com.jumunhasyeo.common.outbox.OutboxRepository;
import com.jumunhasyeo.common.outbox.OutboxStatus;
import com.jumunhasyeo.hub.hub.application.HubEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.jumunhasyeo.common.outbox.OutboxStatus.PENDING;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPollingScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000) // 5초마다 Polling
    public void pollOutbox() {
        // 처리되지 않은 이벤트 조회
        List<Outbox> events = outboxRepository.findTop100ByStatusOrderByIdAsc(PENDING);

        for (Outbox event : events) {
            try {
                // Kafka 발행
                kafkaTemplate.send(event.getEventName(), event.getPayload());
                // 처리 완료 표시
                event.markProcessed();
            } catch (Exception e) {
                log.error("Failed to dispatch event: {}", event.getId(), e);
            }
        }
    }

    @Scheduled(cron = "0 0 3 * * *") // 매일 03:00 7일 지난 완료된 이벤트 정리
    public void cleanupOutbox() {
        int deleted = outboxRepository.deleteByStatusAndCreatedAtBefore(
                OutboxStatus.COMPLETE, LocalDateTime.now().minusDays(7)
        );
        log.info("Deleted {} completed outbox events", deleted);
    }
}
