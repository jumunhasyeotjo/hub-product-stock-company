package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.presentation.dto.HubSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HubCachedDecoratorService implements HubService {
    private final HubServiceImpl hubServiceImpl;

    @Transactional
    public HubRes create(CreateHubCommand command) {
       return hubServiceImpl.create(command);
    }

    @Transactional
    @CachePut(value = "hub", key = "#command.hubId()")
    public HubRes update(UpdateHubCommand command) {
        HubRes update = hubServiceImpl.update(command);
        log.info("Cache Write-Through - hubId: {}", update.id());
        return update;
    }

    @Transactional
    @CacheEvict(value = "hub", key = "#command.hubId()")
    public UUID delete(DeleteHubCommand command) {
        UUID deleteId = hubServiceImpl.delete(command);
        log.info("Cache delete - hubId: {}", deleteId);
        return deleteId;
    }

    @Cacheable(value = "hub", key = "#hubId", unless = "#result == null")
    public HubRes getById(UUID hubId) {
        HubRes hubRes = hubServiceImpl.getById(hubId);
        log.info("Cache get - hubId: {}", hubRes.id());
        return hubRes;
    }

    public Page<HubRes> search(HubSearchCondition condition, Pageable pageable) {
        return hubServiceImpl.search(condition, pageable);
    }
}
