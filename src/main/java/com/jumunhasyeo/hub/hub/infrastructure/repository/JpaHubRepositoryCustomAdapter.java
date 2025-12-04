package com.jumunhasyeo.hub.hub.infrastructure.repository;

import com.jumunhasyeo.hub.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepositoryCustom;
import com.jumunhasyeo.hub.hub.presentation.dto.HubSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaHubRepositoryCustomAdapter implements HubRepositoryCustom {

    private final JpaHubRepositoryCustom jpaHubRepositoryCustom;

    @Override
    public Page<HubRes> searchHubsByCondition(HubSearchCondition condition, Pageable pageable) {
        return jpaHubRepositoryCustom.searchHubsByCondition(condition,pageable);
    }
}
