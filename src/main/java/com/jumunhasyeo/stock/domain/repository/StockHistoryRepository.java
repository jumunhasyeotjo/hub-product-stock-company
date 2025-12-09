package com.jumunhasyeo.stock.domain.repository;

import com.jumunhasyeo.stock.domain.entity.StockHistory;

import java.util.List;
import java.util.UUID;

public interface StockHistoryRepository {
    StockHistory save(StockHistory stockHistory);
    List<StockHistory> saveAll(List<StockHistory> stockHistories);
}
