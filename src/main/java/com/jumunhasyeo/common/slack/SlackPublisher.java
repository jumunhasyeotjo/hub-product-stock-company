package com.jumunhasyeo.common.slack;

import org.springframework.stereotype.Component;

@Component
public class SlackPublisher {
    public void publish(String title, String message) {
        // TODO 실제 Slack 메시지 발송 로직 구현
        System.out.println("Slack Alert - " + title + ": " + message);
    }
}
