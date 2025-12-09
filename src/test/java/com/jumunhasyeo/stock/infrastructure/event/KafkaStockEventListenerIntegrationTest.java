package com.jumunhasyeo.stock.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.CleanUp;
import com.jumunhasyeo.CommonTestContainer;
import com.jumunhasyeo.InternalIntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@SpringBootTest
@Import({CleanUp.class, InternalIntegrationTestConfig.class})
public class KafkaStockEventListenerIntegrationTest extends CommonTestContainer {

    @Autowired
    private KafkaStockEventListener kafkaStockEventListener;

    @MockitoBean
    private OrderCompensateHandler orderCompensateHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.truncateAll();
    }

    @Test
    @DisplayName("OrderCancelEvent를 수신하고 처리할 수 있다.")
    void dispatch_OrderCancelEvent_integration_success() throws Exception {
        //given
        OrderCancelEvent event = new OrderCancelEvent(UUID.randomUUID(), LocalDateTime.now());
        String payload = objectMapper.writeValueAsString(event);
        String simpleClassName = "OrderCancelEvent";

        //when
        kafkaStockEventListener.dispatch(payload, simpleClassName);

        //then
        then(orderCompensateHandler).should().compensate(any(OrderCancelEvent.class));
    }

    @Test
    @DisplayName("OrderRolledBackEvent를 수신하고 처리할 수 있다.")
    void dispatch_OrderRolledBackEvent_integration_success() throws Exception {
        //given
        OrderRolledBackEvent event = new OrderRolledBackEvent(UUID.randomUUID(), LocalDateTime.now());
        String payload = objectMapper.writeValueAsString(event);
        String simpleClassName = "OrderRolledBackEvent";

        //when
        kafkaStockEventListener.dispatch(payload, simpleClassName);

        //then
        then(orderCompensateHandler).should().compensate(any(OrderRolledBackEvent.class));
    }
}
