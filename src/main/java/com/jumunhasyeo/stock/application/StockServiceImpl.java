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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class StockServiceImpl implements StockService{

    private final StockRepository stockRepository;

    @Override
    @Transactional(readOnly = true)
    public StockRes decrement(DecreaseStockCommand command) {
        Stock stock = getStock(command.stockId());
        stockRepository.decreaseStock(stock.getStockId(), command.amount());
        stock.decrease(command.amount());
        return StockRes.from(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public StockRes increment(IncreaseStockCommand command) {
        Stock stock = getStock(command.stockId());
        stockRepository.increaseStock(stock.getStockId(), command.amount());
        stock.increase(command.amount());
        return StockRes.from(stock);
    }

    @Override
    public Stock getStock(UUID stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION));
    }
}
