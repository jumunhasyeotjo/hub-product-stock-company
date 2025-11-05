package com.jumunhasyeo.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class HubProductStockCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubProductStockCompanyApplication.class, args);
    }
}