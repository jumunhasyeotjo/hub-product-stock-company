package com.jumunhasyeo.hub.presentation.dto;

import lombok.*;

import java.util.UUID;

/**
 * Hub 검색 조건 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HubSearchCondition {
    private String name;              // Hub 이름
    private String street;            // 주소
    private UUID productId;           // 특정 상품 ID
    private Integer minStockQuantity; // 최소 재고 수량
}

