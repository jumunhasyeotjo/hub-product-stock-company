package com.jumunhasyeo.product.infrastructure.external;

import com.jumunhasyeo.product.application.service.UserClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserClientImpl implements UserClient {
    @Override
    public Optional<UUID> getOrganizationId(Long userId) {
        return Optional.empty();
    }
}