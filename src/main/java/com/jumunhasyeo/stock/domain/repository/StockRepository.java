package com.jumunhasyeo.stock.domain.repository;

import com.jumunhasyeo.stock.application.dto.response.StockRes;
import com.jumunhasyeo.stock.domain.entity.Stock;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository {

    Optional<Stock> findByProductId(UUID productId);

    Optional<Stock> findByProductIdWithLock(UUID productId);

    boolean decreaseStock(UUID stockId, int quantity);

    boolean increaseStock(UUID stockId, int amount);

    Optional<Stock> findById(UUID stockId);

    Stock save(Stock stock);
}
