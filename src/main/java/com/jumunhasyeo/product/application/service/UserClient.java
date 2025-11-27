package com.jumunhasyeo.product.application.service;

import java.util.Optional;
import java.util.UUID;

public interface UserClient {
    Optional<UUID> getOrganizationId(Long userId);
}
