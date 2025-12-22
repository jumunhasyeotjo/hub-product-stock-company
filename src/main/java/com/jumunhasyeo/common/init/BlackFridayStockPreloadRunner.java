package com.jumunhasyeo.common.init;

import com.jumunhasyeo.stock.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
public class BlackFridayStockPreloadRunner implements ApplicationRunner {

    private final RedisTemplate<String, Object> bfRedisTemplate;
    private final StockRepository stockRepository;

    public BlackFridayStockPreloadRunner(
            @Qualifier("bfRedisTemplate") RedisTemplate<String, Object> bfRedisTemplate,
            StockRepository stockRepository
    ) {
        this.bfRedisTemplate = bfRedisTemplate;
        this.stockRepository = stockRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        stockRepository.findAll().forEach(stock -> {
            String key = "bf:stock:" + stock.getProductId();
            bfRedisTemplate.opsForValue().set(key, stock.getQuantity());
        });
    }
}
