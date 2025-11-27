package com.jumunhasyeo.company.application.command;

import com.jumunhasyeo.company.domain.entity.CompanyType;

import java.util.UUID;

public record CreateCompanyCommand(
        UUID hubId,
        String name,
        CompanyType companyType,
        String address
) {
}
