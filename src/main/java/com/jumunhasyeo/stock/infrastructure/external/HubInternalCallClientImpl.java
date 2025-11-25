package com.jumunhasyeo.stock.infrastructure.external;

import com.jumunhasyeo.hub.application.HubService;
import com.jumunhasyeo.stock.application.service.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubInternalCallClientImpl implements HubClient {
    private final HubService hubService;

    @Override
    public boolean existHub(UUID hubId) {
        return hubService.existById(hubId);
    }
}
