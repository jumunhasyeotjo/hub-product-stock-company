package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.common.BaseEntity;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "p_hub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id", columnDefinition = "UUID")
    private UUID hubId;

    @Column(name = "name", nullable = false, length = 20, unique = true)
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stock> stockList = new HashSet<>();

    private Hub(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public static Hub of(String name, Address address) {
        validate(name, address);
        return new Hub(name, address);
    }

    private static void validate(String name, Address address) {
        if (StringUtils.isEmpty(name) || address == null) {
            throw new BusinessException(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        }
    }

    public void update(String name, Address address) {
        validate(name, address);
        this.name = name;
        this.address = address;
    }

    public void delete(Long userId) {
        if (userId == null)
            throw new BusinessException(ErrorCode.MUST_NOT_NULL, "userId");
        markDeleted(userId);
    }

    public Stock registerNewStock(UUID productId, Integer quantity) {
        // 기존 재고 확인
        Optional<Stock> existingStock = getStock(productId);
        if (existingStock.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS, "해당 상품은 이미 존재합니다.");
        }

        Stock stock = Stock.of(this, productId, quantity);
        this.stockList.add(stock);
        return stock;
    }

    public void addStock(Stock stock) {
        this.stockList.add(stock);
        stock.setHub(this);
    }

    public Stock stockDecrease(UUID productId, int amount) {
        Stock targetStock = getStock(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION, "해당 상품의 재고를 찾을 수 없습니다."));
        targetStock.decrease(amount);
        return targetStock;
    }

    public Stock stockIncrease(UUID productId, int amount) {
        Stock targetStock = getStock(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION, "해당 상품의 재고를 찾을 수 없습니다."));
        targetStock.increase(amount);
        return targetStock;
    }

    public Set<Stock> getStockList() {
        return Collections.unmodifiableSet(stockList);
    }

    public Optional<Stock> getStock(UUID productId) {
        return stockList.stream().filter(stock -> stock.isSameProduct(productId)).findFirst();
    }
}