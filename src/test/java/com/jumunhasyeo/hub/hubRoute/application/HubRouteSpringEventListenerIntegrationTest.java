package com.jumunhasyeo.hub.hubRoute.application;

import com.jumunhasyeo.CleanUp;
import com.jumunhasyeo.CommonTestContainer;
import com.jumunhasyeo.InternalIntegrationTestConfig;
import com.jumunhasyeo.common.outbox.JpaOutboxRepository;
import com.jumunhasyeo.common.outbox.OutboxEvent;
import com.jumunhasyeo.common.outbox.OutboxStatus;
import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hub.domain.entity.HubType;
import com.jumunhasyeo.hub.hub.domain.vo.Address;
import com.jumunhasyeo.hub.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hub.hubRoute.domain.entity.HubRoute;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteCreatedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteDeletedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.vo.RouteWeight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({CleanUp.class, InternalIntegrationTestConfig.class})
public class HubRouteSpringEventListenerIntegrationTest extends CommonTestContainer {

    @Autowired
    private HubRouteSpringEventListener hubRouteSpringEventListener;

    @Autowired
    private JpaOutboxRepository jpaOutboxRepository;

    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.truncateAll();
    }

    @Test
    @DisplayName("HubRouteCreatedEvent 리스트를 수신하면 Outbox에 저장된다.")
    void handleHubRouteCreated_integration_success() {
        //given
        HubRoute route1 = createHubRoute();
        HubRoute route2 = createHubRoute();
        List<HubRouteCreatedEvent> eventList = List.of(
                HubRouteCreatedEvent.from(route1),
                HubRouteCreatedEvent.from(route2)
        );

        //when
        hubRouteSpringEventListener.handleHubRouteCreated(eventList);

        //then
        List<OutboxEvent> outboxEvents = jpaOutboxRepository.findAll();
        assertThat(outboxEvents).hasSize(2);
        assertThat(outboxEvents.get(0).getEventName()).isEqualTo("HubRouteCreatedEvent");
        assertThat(outboxEvents.get(0).getStatus()).isEqualTo(OutboxStatus.PENDING);
    }

    @Test
    @DisplayName("HubRouteDeletedEvent 리스트를 수신하면 Outbox에 저장된다.")
    void handleHubRouteDeleted_integration_success() {
        //given
        HubRoute route1 = createHubRoute();
        HubRoute route2 = createHubRoute();
        List<HubRouteDeletedEvent> eventList = List.of(
                HubRouteDeletedEvent.from(route1),
                HubRouteDeletedEvent.from(route2)
        );

        //when
        hubRouteSpringEventListener.handleHubRouteDeleted(eventList);

        //then
        List<OutboxEvent> outboxEvents = jpaOutboxRepository.findAll();
        assertThat(outboxEvents).hasSize(2);
        assertThat(outboxEvents.get(0).getEventName()).isEqualTo("HubRouteDeletedEvent");
        assertThat(outboxEvents.get(0).getStatus()).isEqualTo(OutboxStatus.PENDING);
    }

    private static HubRoute createHubRoute() {
        Hub startHub = createHub();
        Hub endHub = createHub();
        RouteWeight weight = RouteWeight.of(BigDecimal.valueOf(30), 5);
        
        return HubRoute.builder()
                .routeId(UUID.randomUUID())
                .startHub(startHub)
                .endHub(endHub)
                .routeWeight(weight)
                .build();
    }

    private static Hub createHub() {
        return Hub.builder()
                .hubId(UUID.randomUUID())
                .name("테스트 허브")
                .hubType(HubType.CENTER)
                .address(Address.of("서울시", Coordinate.of(37.5, 127.0)))
                .build();
    }
}
