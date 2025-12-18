package com.jumunhasyeo.hub.hub.infrastructure.repository;

import com.jumunhasyeo.hub.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.hub.presentation.dto.HubSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JpaHubRepositoryCustom {
    Page<HubRes> searchHubsByCondition(HubSearchCondition condition, Pageable pageable);
}
