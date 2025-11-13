package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.hub.application.command.*;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.application.dto.response.StockRes;
import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.entity.Stock;
import com.jumunhasyeo.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.domain.repository.HubRepositoryCustom;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import com.jumunhasyeo.hub.presentation.dto.HubSearchCondition;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HubServiceImpl implements HubService{
    private final HubRepository hubRepository;
    private final HubRepositoryCustom hubRepositoryCustom;
    private final StockService stockService;
    private final ApplicationEventPublisher eventPublisher;
    private final EntityManager entityManager;

    @Transactional
    public HubRes create(CreateHubCommand command) {
        Coordinate coordinate = Coordinate.of(command.latitude(), command.longitude());
        Address address = Address.of(command.address(), coordinate);
        Hub hub = Hub.of(command.name(), address);
        eventPublisher.publishEvent(HubCreatedEvent.of(hub));
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

    public HubRes getById(UUID hubId) {
        return HubRes.from(getHub(hubId));
    }

    public Page<HubRes> search(HubSearchCondition condition, Pageable pageable) {
        return hubRepositoryCustom.searchHubsByCondition(condition, pageable);
    }

    @Transactional
    public StockRes decreaseStock(DecreaseStockCommand command) {;
        UUID productId = command.productId();
        int amount = command.amount();
        Hub hub = getHubWithStockByProductId(productId);

        entityManager.detach(hub);
        Stock stock = hub.stockDecrease(productId, amount);
        stockService.tryDecreaseStockAtomically(stock.getStockId(), amount);
        return StockRes.from(stock);
    }

    @Transactional
    public StockRes increaseStock(IncreaseStockCommand command) {
        UUID productId = command.productId();
        int amount = command.amount();
        Hub hub = getHubWithStockByProductId(productId);
        Stock stock = hub.stockIncrease(productId, amount);

        entityManager.detach(hub);
        stockService.tryIncreaseStock(stock.getStockId(), amount);
        return StockRes.from(stock);
    }

    private Hub getHubWithStockByProductId(UUID productId) {
        Stock stock = hubRepository.findStockByProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION));
        return stock.getHub();
    }

    private Hub getHub(UUID hubId) {
        return hubRepository.findById(hubId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION));
    }
}
