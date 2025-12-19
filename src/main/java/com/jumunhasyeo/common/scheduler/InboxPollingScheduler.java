package com.jumunhasyeo.common.scheduler;

import com.jumunhasyeo.common.inbox.InboxEvent;
import com.jumunhasyeo.common.inbox.InboxService;
import com.jumunhasyeo.common.inbox.InboxStatus;
import com.jumunhasyeo.common.inbox.JpaInboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InboxPollingScheduler {

    private final InboxService inboxService;
    private final JpaInboxRepository inboxRepository;

    /**
     * PROCESSING 상태의 이벤트 재처리
     * 3초마다 폴링
     */
    @Async("schedulerExecutor")
    @SchedulerLock(name = "inboxPolling", lockAtLeastFor = "3s")
    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void retryProcessingEvents() {
        // PROCESSING 상태이면서 10초 이상 지난 이벤트 조회
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(10);
        List<InboxEvent> stuckEvents = inboxService.findByStatusAndModifiedAtBefore(InboxStatus.PROCESSING, threshold);

        if (!stuckEvents.isEmpty()) {
            log.info("Found {} stuck PROCESSING events", stuckEvents.size());
        }

        for (InboxEvent event : stuckEvents) {
            inboxService.inboxProcess(event);
        }
    }

    @Async("schedulerExecutor")
    @SchedulerLock(name = "inboxPollingCleanup")
    @Scheduled(cron = "0 0 3 * * *") // 매일 03:00 7일 지난 완료된 이벤트 정리
    @Transactional
    public void cleanupCompletedEvents() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        
        int deleted = inboxRepository.deleteByStatusAndModifiedAtBefore(
                InboxStatus.COMPLETED, cutoff
        );
        
        log.info("Deleted {} completed inbox events", deleted);
    }
}
