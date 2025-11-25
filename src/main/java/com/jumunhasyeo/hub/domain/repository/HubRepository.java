package com.jumunhasyeo.hub.domain.repository;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.entity.HubType;
import com.jumunhasyeo.hubRoute.domain.entity.HubRoute;
import com.querydsl.core.Fetchable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    Hub save(Hub hub);
    Optional<Hub> findById(UUID id);
    Boolean existById(UUID uuid);
    long count();
    List<Hub> findAllByHubType(HubType type);
    List<Hub> findAll();
}