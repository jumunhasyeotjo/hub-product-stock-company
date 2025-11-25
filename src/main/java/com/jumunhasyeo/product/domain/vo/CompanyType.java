package com.jumunhasyeo.product.domain.vo;

import lombok.Getter;

@Getter
public enum CompanyType {
    SUPPLY("공급 업체"),
    RECEIVE("수령 업체");

    private final String description;

    CompanyType(String description) {
        this.description = description;
    }
}
