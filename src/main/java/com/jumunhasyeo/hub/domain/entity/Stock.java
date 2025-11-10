package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.common.BaseEntity;
import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
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

    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    private Stock(Hub hub, UUID productId, Integer quantity) {
        this.hub = hub;
        this.productId = productId;
        this.quantity = quantity;
    }

    static Stock of(Hub hub, UUID productId, Integer quantity) {
        if (hub == null || productId == null || quantity == null || quantity < 0)
            throw new BusinessException(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        return new Stock(hub, productId, quantity);
    }

    void decrease(int amount) {
        if (amount <= 0)
            throw new BusinessException(ErrorCode.STOCK_VALID, "감소 수량은 0보다 커야 합니다.");

        if (quantity < amount)
            throw new BusinessException(ErrorCode.STOCK_VALID, "재고가 부족합니다.");

        this.quantity -= amount;
    }

    void increase(int amount) {
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
