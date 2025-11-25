package com.jumunhasyeo.hub.domain.repository;

import com.jumunhasyeo.hub.domain.entity.Hub;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    Hub save(Hub hub);

    Optional<Hub> findById(UUID id);

    Boolean existById(UUID uuid);
}