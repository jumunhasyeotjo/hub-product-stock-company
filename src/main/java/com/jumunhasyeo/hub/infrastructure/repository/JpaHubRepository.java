package com.jumunhasyeo.hub.infrastructure.repository;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

    @Query("SELECT h FROM Hub h WHERE h.hubId = :id AND h.deletedAt IS NULL")
    Optional<Hub> findById(@Param("id") UUID id);

    @Query("SELECT s FROM Stock s " +
            "WHERE s.productId = :productId " +
            "AND s.deletedAt IS NULL")
    Optional<Stock> findStockByProductId(UUID productId);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.quantity = s.quantity - :amount WHERE s.quantity - :amount >= 0 AND s.stockId = :stockId")
    int decreaseStock(@Param(value = "stockId") UUID stockId, @Param(value = "amount") int amount);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.quantity = s.quantity + :amount WHERE s.stockId = :stockId AND s.quantity + :amount <= 2147483647")
    int increaseStock(UUID stockId, int amount);
}
