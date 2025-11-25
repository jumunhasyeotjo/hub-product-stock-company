package com.jumunhasyeo.stock.domain.entity;

import com.jumunhasyeo.common.BaseEntity;
import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "stock_id", columnDefinition = "UUID")
    private UUID stockId;

    @Column(name = "product_id", columnDefinition = "UUID", unique = true)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    private Stock(UUID hubId, UUID productId, Integer quantity) {
        this.hubId = hubId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Stock of(UUID hubId, UUID productId, Integer quantity) {
        if (hubId == null || productId == null || quantity == null || quantity < 0)
            throw new BusinessException(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        return new Stock(hubId, productId, quantity);
    }

    public void decrease(int amount) {
        if (amount <= 0)
            throw new BusinessException(ErrorCode.STOCK_VALID, "감소 수량은 0보다 커야 합니다.");

        if (quantity < amount)
            throw new BusinessException(ErrorCode.STOCK_VALID, "재고가 부족합니다.");

        this.quantity -= amount;
    }

    public void increase(int amount) {
        if (amount <= 0)
            throw new BusinessException(ErrorCode.STOCK_VALID, "증가 수량은 0보다 커야 합니다.");

        if(this.quantity > Integer.MAX_VALUE - amount){
            throw new BusinessException(ErrorCode.STOCK_VALID, "재고 최대값을 초과했습니다.");
        }

        this.quantity += amount;
    }

    public boolean isSameProduct(UUID productId) {
        if (productId == null) return false;
        return this.productId.equals(productId);
    }
}
