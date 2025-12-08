package com.jumunhasyeo.hub.hubRoute.domain.repository;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hubRoute.domain.entity.HubRoute;

import java.util.List;
import java.util.Set;

public interface HubRouteRepository {
    void save(HubRoute forwardRoute);
    void saveAll(List<HubRoute> routes);
    void insertIgnore(Set<HubRoute> createAllRoute);
    List<HubRoute> findAll();
    List<HubRoute> findByStartHubOrEndHub(Hub startHub, Hub endHub);
}
