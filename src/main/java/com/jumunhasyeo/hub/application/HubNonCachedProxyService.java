package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.presentation.dto.HubSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HubNonCachedProxyService implements HubService {
    private final HubServiceImpl hubServiceImpl;

    @Transactional
    public HubRes create(CreateHubCommand command) {
       return hubServiceImpl.create(command);
    }

    @Transactional
    public HubRes update(UpdateHubCommand command) {
        return hubServiceImpl.update(command);
    }

    @Transactional
    public UUID delete(DeleteHubCommand command) {
        return hubServiceImpl.delete(command);
    }

    public HubRes getById(UUID hubId) {
        System.out.println("non");
        return hubServiceImpl.getById(hubId);
    }

    public Page<HubRes> search(HubSearchCondition condition, Pageable pageable) {
        return hubServiceImpl.search(condition, pageable);
    }
}
