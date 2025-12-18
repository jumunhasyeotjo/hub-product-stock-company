package com.jumunhasyeo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(InternalIntegrationTestConfig.class)
class HubProductStockCompanyApplicationTests extends CommonTestContainer {

    @Test
    void contextLoads() {
    }

}
