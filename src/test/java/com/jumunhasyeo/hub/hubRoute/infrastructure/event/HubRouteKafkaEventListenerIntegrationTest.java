package com.jumunhasyeo.hub.hubRoute.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.CleanUp;
import com.jumunhasyeo.CommonTestContainer;
import com.jumunhasyeo.InternalIntegrationTestConfig;
import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hub.domain.entity.HubType;
import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.hub.domain.vo.Address;
import com.jumunhasyeo.hub.hub.domain.vo.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@SpringBootTest
@Import({CleanUp.class, InternalIntegrationTestConfig.class})
public class HubRouteKafkaEventListenerIntegrationTest extends CommonTestContainer {

    @Autowired
    private HubRouteKafkaEventListener hubRouteKafkaEventListener;

    @MockitoBean
    private HubRouteEventHandler hubRouteEventHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.truncateAll();
    }

    @Test
    @DisplayName("HubCreatedEvent를 수신하고 처리할 수 있다.")
    void dispatch_HubCreatedEvent_integration_success() throws Exception {
        //given
        Hub hub = createHub();
        HubCreatedEvent event = HubCreatedEvent.centerHub(hub);
        String payload = objectMapper.writeValueAsString(event);
        String simpleClassName = "HubCreatedEvent";

        //when
        hubRouteKafkaEventListener.dispatch(payload, simpleClassName);

        //then
        then(hubRouteEventHandler).should().hubCreated(any(HubCreatedEvent.class));
    }

    @Test
    @DisplayName("HubDeletedEvent를 수신하고 처리할 수 있다.")
    void dispatch_HubDeletedEvent_integration_success() throws Exception {
        //given
        Hub hub = createHub();
        HubDeletedEvent event = HubDeletedEvent.from(hub, 1L);
        String payload = objectMapper.writeValueAsString(event);
        String simpleClassName = "HubDeletedEvent";

        //when
        hubRouteKafkaEventListener.dispatch(payload, simpleClassName);

        //then
        then(hubRouteEventHandler).should().hubDeleted(any(HubDeletedEvent.class));
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
