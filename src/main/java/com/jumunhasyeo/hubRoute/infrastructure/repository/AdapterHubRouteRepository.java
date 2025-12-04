package com.jumunhasyeo.hubRoute.infrastructure.repository;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hubRoute.domain.entity.HubRoute;
import com.jumunhasyeo.hubRoute.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AdapterHubRouteRepository implements HubRouteRepository {
    private final JpaHubRouteRepositoryImpl repository;

    @Override
    public void save(HubRoute forwardRoute) {
        repository.save(forwardRoute);
    }

    @Override
    public void saveAll(List<HubRoute> routes) {
        repository.saveAll(routes);
    }

    @Override
    public void insertIgnore(Set<HubRoute> createAllRoute) {
        for (HubRoute hubRoute : createAllRoute) {
            try {
                repository.save(hubRoute);
            }catch (DataIntegrityViolationException e) {
                log.warn("Duplicate hub route detected, ignoring: {}", hubRoute);
            }
        }
    }

    @Override
    public List<HubRoute> findAll() {
        return repository.findAll();
    }

    @Override
    public List<HubRoute> findByStartHubOrEndHub(Hub startHub, Hub endHub) {
        return repository.findByStartHubOrEndHub(startHub, endHub);
    }
}
