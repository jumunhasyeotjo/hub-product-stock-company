package com.jumunhasyeo.common.scheduler;

import com.jumunhasyeo.common.outbox.OutboxEvent;
import com.jumunhasyeo.common.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.jumunhasyeo.common.outbox.OutboxStatus.PENDING;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPollingScheduler {

    private final OutboxService outboxService;

    @Async("schedulerExecutor")
    @SchedulerLock(name = "outboxPolling", lockAtLeastFor = "5s")
    @Scheduled(fixedDelay = 5000) // 2초마다 Polling
    public void pollOutbox() {
        // 처리되지 않은 이벤트 조회
        List<OutboxEvent> events = outboxService.findTop100ByStatusOrderByIdAsc(PENDING);
        for (OutboxEvent event : events) {
            outboxService.outboxProcess(event);
        }
    }



    @Async("schedulerExecutor")
    @SchedulerLock(name = "outboxPollingCleanup")
    @Scheduled(cron = "0 0 3 * * *") // 매일 03:00 7일 지난 완료된 이벤트 정리
    public void cleanupOutbox() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        int deletedComplete = outboxService.cleanUp(cutoff);
        log.info("Deleted {} completed outbox events", deletedComplete);
    }
}
