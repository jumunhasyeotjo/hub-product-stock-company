package com.jumunhasyeo.stock.application;

import com.jumunhasyeo.common.Idempotency.Idempotent;
import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.stock.application.command.CreateStockCommand;
import com.jumunhasyeo.stock.application.command.DecreaseStockCommand;
import com.jumunhasyeo.stock.application.command.DeleteStockCommand;
import com.jumunhasyeo.stock.application.command.IncreaseStockCommand;
import com.jumunhasyeo.stock.application.dto.response.StockRes;
import com.jumunhasyeo.stock.application.service.HubClient;
import com.jumunhasyeo.stock.application.service.ProductClient;
import com.jumunhasyeo.stock.domain.entity.Stock;
import com.jumunhasyeo.stock.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockVariationService stockVariationService;
    private final StockRepository stockRepository;
    private final HubClient hubClient;
    private final ProductClient productClient;

    //재고 추가
    @Transactional
    public StockRes create(CreateStockCommand command){
        if(!isExistHubAndProduct(command)){
            throw new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION, "허브 또는 상품이 존재하지 않습니다.");
        }
        Stock stock = Stock.of(command.hubId(), command.productId(), command.quantity());
        Stock save = stockRepository.save(stock);
        return StockRes.from(save);
    }

    //단건 조회
    public StockRes get(UUID stockId){
        Stock save = getStock(stockId);
        return StockRes.from(save);
    }

    //삭제
    @Transactional
    public StockRes delete(DeleteStockCommand command) {
        Stock stock = getStock(command.stockId());
        stock.markDeleted(command.userId());
        return StockRes.from(stock);
    }

    //상품 재고 감소
    @Idempotent(ttlDays = 1)
    public StockRes decrement(String idempotencyKey,DecreaseStockCommand command){
        return stockVariationService.decrement(command);
    }

    //상품 재고 증가
    @Idempotent(ttlDays = 1)
    public StockRes increment(String idempotencyKey, IncreaseStockCommand command){
        return stockVariationService.increment(command);
    }

    private Stock getStock(UUID stockId){
        return stockRepository.findById(stockId)
                .orElseThrow(()-> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    private boolean isExistHubAndProduct(CreateStockCommand command) {
        return hubClient.existHub(command.hubId()) && productClient.existProduct(command.productId());
    }
}
