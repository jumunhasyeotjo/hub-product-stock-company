package com.jumunhasyeo.hub.hub.application;

import com.jumunhasyeo.hub.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.hub.presentation.dto.HubSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface HubService {
    //허브 생성
    HubRes create(CreateHubCommand command);
    //허브 수정
    HubRes update(UpdateHubCommand command);
    //허브 삭제
    UUID delete(DeleteHubCommand command);
    //허브 단건 조회
    HubRes getById(UUID hubId);
    //허브 검색 조회
    Page<HubRes> search(HubSearchCondition condition, Pageable pageable);
    //존재 하는지 조회
    Boolean existById(UUID hubId);
    //허브 전체 조회
    List<HubRes> getAll();
}