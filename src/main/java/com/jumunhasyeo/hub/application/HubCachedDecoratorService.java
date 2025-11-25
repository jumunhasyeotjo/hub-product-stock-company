package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.presentation.dto.HubSearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Primary
@Slf4j
public class HubCachedDecoratorService implements HubService {

    private static final String CACHE_NAME = "hub";

    private final HubService hubService;

    public HubCachedDecoratorService(@Qualifier("hubServiceImpl") HubService hubService) {
        this.hubService = hubService;
    }

    /**
     * Hub 생성 후 캐시에 저장
     * - 생성된 Hub의 ID를 키로 사용
     */
    @Transactional
    @CachePut(value = CACHE_NAME, key = "#result.id()", condition = "#result != null")
    public HubRes create(CreateHubCommand command) {
        HubRes created = hubService.create(command);
        log.info("Cache Write-Through on CREATE - hubId: {}", created.id());
        return created;
    }

    /**
     * Hub 수정 후 캐시 업데이트
     * - Write-Through 전략
     */
    @Transactional
    @CachePut(value = CACHE_NAME, key = "#command.hubId()", condition = "#result != null")
    public HubRes update(UpdateHubCommand command) {
        HubRes updated = hubService.update(command);
        log.info("Cache Write-Through on UPDATE - hubId: {}", updated.id());
        return updated;
    }

    /**
     * Hub 삭제 시 캐시 제거
     * - Cache Eviction
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.hubId()")
    public UUID delete(DeleteHubCommand command) {
        UUID deletedId = hubService.delete(command);
        log.info("Cache EVICT on DELETE - hubId: {}", deletedId);
        return deletedId;
    }

    /**
     * Hub 단건 조회 - 캐시 우선
     * - Cache-Aside (Lazy Loading) 전략
     * - 캐시 미스 시에만 DB 조회
     */
    @Cacheable(value = CACHE_NAME, key = "#hubId", unless = "#result == null")
    public HubRes getById(UUID hubId) {
        log.info("Cache MISS - Loading from DB - hubId: {}", hubId);
        HubRes hubRes = hubService.getById(hubId);
        log.info("Loaded Hub from DB and cached - hubId: {}", hubRes.id());
        return hubRes;
    }

    /**
     * Hub 검색 - 캐시 사용 안 함
     * - 검색 조건이 다양하여 캐시 효율 낮음
     * - 페이징 결과는 자주 변경됨
     */
    public Page<HubRes> search(HubSearchCondition condition, Pageable pageable) {
        return hubService.search(condition, pageable);
    }

    @Override
    public Boolean existById(UUID hubId) {
        return hubService.existById(hubId);
    }

    @Override
    public List<HubRes> getAll() {
        return hubService.getAll();
    }
}