package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.common.BaseEntity;
import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private UUID id;

    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    private Stock(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static Stock of(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity < 0)
            throw new BusinessException(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        return new Stock(productId, quantity);
    }

    public void decrease(int amount) {
        if (amount <= 0)
            throw new BusinessException(ErrorCode.STOCK_VALID, "감소 수량은 0보다 커야 합니다.");

        if (quantity < amount)
            throw new BusinessException(ErrorCode.STOCK_VALID, "재고가 부족합니다.");

        this.quantity -= amount;
    }

    public boolean isSameProduct(Long productId) {
        return this.productId.equals(productId);
    }

    public void increase(int amount) {
        if (amount <= 0)
            throw new BusinessException(ErrorCode.STOCK_VALID, "증가 수량은 0보다 커야 합니다.");

        this.quantity += amount;
    }
}
