package com.jumunhasyeo.product.infrastructure.external;

import com.jumunhasyeo.product.application.service.CompanyClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "company-service")
public interface CompanyClientImpl extends CompanyClient {
}
