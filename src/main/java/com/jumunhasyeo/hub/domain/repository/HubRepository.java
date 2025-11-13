package com.jumunhasyeo.hub.domain.repository;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.entity.Stock;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository {
    Hub save(Hub hub);

    Optional<Hub> findById(UUID id);

    Optional<Stock> findStockByProductId(UUID productId);

    int decreaseStock(UUID stockId, int quantity);

    int increaseStock(UUID stockId, int amount);
}