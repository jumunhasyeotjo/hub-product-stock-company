package com.jumunhasyeo.product.infrastructure.external;

import com.jumunhasyeo.company.application.CompanyService;
import com.jumunhasyeo.product.application.service.CompanyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyClientImpl implements CompanyClient {

    private final CompanyService companyService;

    // Todo : Company 도메인 완성시 Internal 로 구현
    @Override
    public boolean existsCompany(UUID organizationId) {
        return companyService.existsById(organizationId);
    }
}
