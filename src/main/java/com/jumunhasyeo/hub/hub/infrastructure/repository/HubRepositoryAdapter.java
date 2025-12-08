package com.jumunhasyeo.hub.hub.infrastructure.repository;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hub.domain.entity.HubType;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {
    private final JpaHubRepository jpaHubRepository;

    @Override
    public Hub save(Hub hub) {
        return jpaHubRepository.save(hub);
    }

    @Override
    public Optional<Hub> findById(UUID id) {
        return jpaHubRepository.findById(id);
    }

    @Override
    public Boolean existById(UUID hubId) {
        return jpaHubRepository.existsById(hubId);
    }

    @Override
    public long count() {
        return jpaHubRepository.count();
    }

    @Override
    public List<Hub> findAllByHubType(HubType type) {
        return jpaHubRepository.findAllByHubType(type);
    }

    @Override
    public List<Hub> findAll() {
        return jpaHubRepository.findAll();
    }
}
