package com.jumunhasyeo.company.application.command;

import com.jumunhasyeo.company.domain.entity.CompanyType;

import java.util.UUID;

public record UpdateCompanyCommand(
        UUID companyId,
        UUID hubId,
        String name,
        CompanyType companyType,
        String address
) {
}
