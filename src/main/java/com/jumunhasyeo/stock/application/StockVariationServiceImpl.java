package com.jumunhasyeo.stock.application;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.stock.application.command.DecreaseStockCommand;
import com.jumunhasyeo.stock.application.command.IncreaseStockCommand;
import com.jumunhasyeo.stock.application.dto.response.StockRes;
import com.jumunhasyeo.stock.domain.entity.Stock;
import com.jumunhasyeo.stock.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class StockVariationServiceImpl implements StockVariationService {

    private final StockRepository stockRepository;

    @Override
    public StockRes decrement(DecreaseStockCommand command) {
        Stock stock = getStock(command.stockId());
        stock.decrease(command.amount());
        StockRes res = StockRes.from(stock);
        stockRepository.decreaseStock(stock.getStockId(), command.amount());
        return res;
    }

    @Override
    public StockRes increment(IncreaseStockCommand command) {
        Stock stock = getStock(command.stockId());
        stock.increase(command.amount());
        StockRes res = StockRes.from(stock);
        stockRepository.increaseStock(stock.getStockId(), command.amount());
        return res;
    }

    private Stock getStock(UUID stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION));
    }
}
