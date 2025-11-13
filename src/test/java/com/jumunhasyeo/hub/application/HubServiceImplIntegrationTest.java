package com.jumunhasyeo.hub.application;

import com.jumunhasyeo.CleanUp;
import com.jumunhasyeo.CommonTestContainer;
import com.jumunhasyeo.hub.application.command.DecreaseStockCommand;
import com.jumunhasyeo.hub.application.command.IncreaseStockCommand;
import com.jumunhasyeo.hub.application.dto.response.StockRes;
import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.entity.Stock;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class HubServiceImplIntegrationTest extends CommonTestContainer {
    @Autowired
    private HubService hubService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CleanUp cleanUp;

    @Test
    @DisplayName("decreaseStock() 실행시 변경 감지를 차단해 중복 쿼리가 발생되지 않는다.")
    public void decreaseStock_실행시_변경_감지를_차단해_중복_쿼리가_발생되지_않는다() {
        //given
        UUID productId = UUID.randomUUID();
        Stock stock = createStock(productId, 500);
        entityManager.persist(stock.getHub());
        entityManager.flush();
        DecreaseStockCommand command = new DecreaseStockCommand(stock.getProductId(), 100);

        // 쿼리 발생 횟수 검증을 위한 Hibernate Statistics 초기화
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class)
                .getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        //when
        StockRes stockRes = transactionTemplate.execute(status -> {
            return hubService.decreaseStock(command);
        });
        //then
        assertThat(stockRes.stockId()).isEqualTo(stock.getStockId());
        assertThat(stockRes.quantity()).isEqualTo(400);
        assertThat(stockRes.hubId()).isEqualTo(stock.getHub().getHubId());
        assertThat(stockRes.productId()).isEqualTo(stock.getProductId());
        assertThat(statistics.getQueryExecutionCount()) //  UPDATE 쿼리가 1번만 발생했는지 검증
                .as("UPDATE 쿼리는 1번만 발생해야 합니다")
                .isEqualTo(1);
    }

    @Test
    @DisplayName("increaseStock() 실행시 변경 감지를 차단해 중복 쿼리가 발생되지 않는다.")
    public void increaseStock_실행시_변경_감지를_차단해_중복_쿼리가_발생되지_않는다() {
        //given
        UUID productId = UUID.randomUUID();
        Stock stock = createStock(productId, 500);
        entityManager.persist(stock.getHub());
        entityManager.flush();
        IncreaseStockCommand command = new IncreaseStockCommand(stock.getProductId(), 100);

        // 쿼리 발생 횟수 검증을 위한 Hibernate Statistics 초기화
        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class)
                .getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        //when
        StockRes stockRes = transactionTemplate.execute(status -> {
            return hubService.increaseStock(command);
        });
        //then
        assertThat(stockRes.stockId()).isEqualTo(stock.getStockId());
        assertThat(stockRes.quantity()).isEqualTo(600);
        assertThat(stockRes.hubId()).isEqualTo(stock.getHub().getHubId());
        assertThat(stockRes.productId()).isEqualTo(stock.getProductId());
        assertThat(statistics.getQueryExecutionCount()) //  UPDATE 쿼리가 1번만 발생했는지 검증
                .as("UPDATE 쿼리는 1번만 발생해야 합니다")
                .isEqualTo(1);
    }

    private Stock createStock(UUID productId, int quantity) {
        Hub hub = createHub();
        Stock stock = Stock.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
        hub.addStock(stock);
        return stock;
    }

    private  Hub createHub() {
        return Hub.builder()
                .stockList(new HashSet<>())
                .name("송파 허브")
                .address(Address.of("street", Coordinate.of(12.6, 12.6)))
                .build();
    }
}
