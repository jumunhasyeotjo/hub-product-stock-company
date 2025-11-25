package com.jumunhasyeo.company.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyType {
    PRODUCER("생산업체"), RECEIVER("수령업체");

    private final String description;
}
