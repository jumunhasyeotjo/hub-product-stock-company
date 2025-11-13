package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class StockService { //package-private

    private final HubRepository hubRepository;

    @Transactional
    public boolean tryDecreaseStockAtomically(UUID stockId, int amount) {
        boolean isSuccess = hubRepository.decreaseStock(stockId, amount) == 1;
        if (!isSuccess)
            throw new BusinessException(ErrorCode.STOCK_VALID, "재고가 부족합니다.");
        return isSuccess;
    }

    @Transactional
    public boolean tryIncreaseStock(UUID stockId, int amount){
        boolean isSuccess = hubRepository.increaseStock(stockId, amount) == 1;
        if (!isSuccess)
            throw new BusinessException(ErrorCode.STOCK_VALID, "최대 재고값을 넘었습니다.");
        return isSuccess;
    }
}
