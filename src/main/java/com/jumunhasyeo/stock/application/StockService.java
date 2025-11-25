package com.jumunhasyeo.stock.application;

import com.jumunhasyeo.stock.application.command.DecreaseStockCommand;
import com.jumunhasyeo.stock.application.command.IncreaseStockCommand;
import com.jumunhasyeo.stock.application.dto.response.StockRes;
import com.jumunhasyeo.stock.domain.entity.Stock;

import java.util.UUID;

public interface StockService {
    //상품 재고 감소
    StockRes decrement(DecreaseStockCommand command);
    //상품 재고 증가
    StockRes increment(IncreaseStockCommand command);
    //재고 조회
    Stock getStock(UUID productId);
}
