package com.jumunhasyeo.common.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OutboxDispatcher {
    @Value("${spring.kafka.topics.hub}")
    private String hubTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void dispatch(OutboxEvent event) {
        if(event.getTopic().equals(hubTopic)) {
            try {
                kafkaTemplate.send(hubTopic, event.getPayload()).get(); // 동기 대기
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            throw new RuntimeException( "Unknown topic: " + event.getTopic());
        }
    }
}
