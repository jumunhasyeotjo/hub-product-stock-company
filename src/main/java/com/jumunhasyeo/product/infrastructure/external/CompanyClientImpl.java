package com.jumunhasyeo.product.infrastructure.external;

import com.jumunhasyeo.product.application.service.CompanyClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyClientImpl implements CompanyClient {

    // Todo : Company 도메인 완성시 Internal 로 구현
    @Override
    public boolean existsCompany(UUID organizationId) {
        return true;
    }
}
