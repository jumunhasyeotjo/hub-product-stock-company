package com.jumunhasyeo.product.infrastructure.external;

import com.jumunhasyeo.product.application.service.UserClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClientImpl extends UserClient {
}
