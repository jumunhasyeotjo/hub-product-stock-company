package com.jumunhasyeo.stock.infrastructure.repository;

import com.jumunhasyeo.stock.domain.entity.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface JpaStockRepository extends JpaRepository<Stock, UUID> {
    @Query("SELECT s FROM Stock s WHERE s.stockId = :id AND s.isDeleted = false")
    Optional<Stock> findById(@Param("id") UUID id);

    @Query("SELECT s FROM Stock s " +
            "WHERE s.productId = :productId " +
            "AND s.isDeleted = false")
    Optional<Stock> findByProductId(UUID productId);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.quantity = s.quantity - :amount WHERE s.quantity - :amount >= 0 AND s.stockId = :stockId")
    int decreaseStock(UUID stockId, int amount);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.quantity = s.quantity + :amount WHERE s.stockId = :stockId AND s.quantity + :amount <= 2147483647")
    int increaseStock(UUID stockId, int amount);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT s FROM Stock s " +
            "WHERE s.productId = :productId " +
            "AND s.isDeleted = false")
    Optional<Stock> findStockByProductIdWithLock(UUID productId);
}
