package com.jumunhasyeo.hub.hubRoute.infrastructure.event;

import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Deprecated
@Slf4j
@Component
@RequiredArgsConstructor
public class HubRouteEventListener {

    private final HubRouteEventHandler hubRouteEventHandler;

    /**
     * Hub 생성 이벤트 처리
     * 트랜잭션 커밋 전 실행
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleHubCreated(HubCreatedEvent event) {
        log.info("Hub created event received: {} (ID: {})",
                event.getName(),
                event.getHubId());

        hubRouteEventHandler.hubCreated(event);
    }

    /**
     * Hub 삭제 이벤트 처리
     * 트랜잭션 커밋 전 실행
     * 
     * Hub가 삭제되면 관련된 모든 경로를 삭제
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleHubDeleted(HubDeletedEvent event) {
        log.info("Hub deleted event received: {} (ID: {})",
                event.getName(),
                event.getHubId());

        hubRouteEventHandler.hubDeleted(event);
        log.info("All routes for hub {} have been deleted", event.getName());
    }
}
