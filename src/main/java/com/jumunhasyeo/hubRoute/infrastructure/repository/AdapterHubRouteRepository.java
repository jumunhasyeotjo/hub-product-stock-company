package com.jumunhasyeo.hubRoute.infrastructure.repository;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hubRoute.domain.entity.HubRoute;
import com.jumunhasyeo.hubRoute.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
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
    public void insertAllIgnore(Set<HubRoute> createAllRoute) {
        for (HubRoute hubRoute : createAllRoute) {
            repository.insertIgnore(
                    hubRoute.getStartHub().getHubId(),
                    hubRoute.getEndHub().getHubId(),
                    hubRoute.getRouteWeight().getDistanceKm().doubleValue(),
                    hubRoute.getRouteWeight().getDurationMinutes()
            );
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
