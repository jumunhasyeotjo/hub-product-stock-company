package com.jumunhasyeo.hub.hub.presentation.dto;

import lombok.*;

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

}

