package com.jumunhasyeo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class HubProductStockCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubProductStockCompanyApplication.class, args);
    }
}