package com.jumunhasyeo.common.init;

import com.jumunhasyeo.stock.domain.entity.Stock;
import com.jumunhasyeo.stock.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

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
        int BATCH_SIZE = 5_000;
        PageRequest page = PageRequest.of(0, BATCH_SIZE);

        while (true) {
            Page<Stock> stocks = stockRepository.findAll(page);
            if (stocks.isEmpty()) break;

            preloadBatch(stocks);
            page = page.next();
        }
    }

    private void preloadBatch(Page<Stock> stocks) {
        bfRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Stock stock : stocks) {
                String key = "bf:stock:" + stock.getProductId();
                byte[] k = bfRedisTemplate.getStringSerializer().serialize(key);
                byte[] v = String.valueOf(stock.getQuantity()).getBytes(StandardCharsets.UTF_8);
                connection.set(k, v);
            }
            return null;
        });

    }
}
