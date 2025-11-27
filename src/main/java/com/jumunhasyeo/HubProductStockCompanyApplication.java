package com.jumunhasyeo;

import com.library.passport.config.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = {
        "com.jumunhasyeo",
        "com.library.passport"
})
@Import(WebMvcConfig.class)
@SpringBootApplication
public class HubProductStockCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HubProductStockCompanyApplication.class, args);
    }
}