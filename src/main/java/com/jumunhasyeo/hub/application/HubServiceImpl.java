package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.domain.repository.HubRepositoryCustom;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hub.presentation.dto.HubSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class HubServiceImpl implements HubService{
    private final HubRepository hubRepository;
    private final HubRepositoryCustom hubRepositoryCustom;
    private final HubEventPublisher hubEventPublisher;

    @Transactional
    public HubRes create(CreateHubCommand command) {
        Coordinate coordinate = Coordinate.of(command.latitude(), command.longitude());
        Address address = Address.of(command.address(), coordinate);
        Hub hub = Hub.of(command.name(), address);
        hubEventPublisher.publishEvent(HubCreatedEvent.of(hub));
        log.info("[HubCreatedEvent] Publish - 허브가 생성되었습니다.");
        hubRepository.save(hub);
        return HubRes.from(hub);
    }

    @Transactional
    public HubRes update(UpdateHubCommand command) {
        Hub hub = getHub(command.hubId());
        Coordinate coordinate = Coordinate.of(command.latitude(), command.longitude());
        Address address = Address.of(command.address(), coordinate);
        hub.update(command.name(), address);
        return HubRes.from(hub);
    }

    @Transactional
    public UUID delete(DeleteHubCommand command) {
        Hub hub = getHub(command.hubId());
        hub.markDeleted(command.userId());
        return hub.getHubId();
    }

    @Override
    public Boolean existById(UUID uuid) {
        return hubRepository.existById(uuid);
    }

    public HubRes getById(UUID hubId) {
        return HubRes.from(getHub(hubId));
    }

    public Page<HubRes> search(HubSearchCondition condition, Pageable pageable) {
        return hubRepositoryCustom.searchHubsByCondition(condition, pageable);
    }

    private Hub getHub(UUID hubId) {
        return hubRepository.findById(hubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION));
    }
}
