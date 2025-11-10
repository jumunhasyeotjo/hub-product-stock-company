package com.jumunhasyeo.hub.domain.repository;

import com.jumunhasyeo.CleanUp;
import com.jumunhasyeo.CommonTestContainer;
import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hub.infrastructure.repository.HubRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({HubRepositoryAdapter.class, CleanUp.class})
class HubRepositoryTest extends CommonTestContainer {

    @Autowired
    private HubRepository hubRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.truncateAll();
    }

    @Test
    @DisplayName("hub를 저장할 수 있다.")
    void save_hub_success() {
        // given
        Hub saveHub = hubRepository.save(createHub());

        // when
        Optional<Hub> findHub = hubRepository.findById(saveHub.getHubId());

        // then
        assertThat(findHub).isNotNull();
        assertThat(findHub.get().getHubId()).isEqualTo(saveHub.getHubId());
    }

    @Test
    @DisplayName("hub를 id로 조회할 수 있다.")
    public void findById_hub_success() {
        Hub savedHub = createHub();
        testEntityManager.persistAndFlush(savedHub);

        Optional<Hub> findHub = hubRepository.findById(savedHub.getHubId());

        assertThat(findHub).isNotNull();
        assertThat(findHub.get().getHubId()).isEqualTo(savedHub.getHubId());
    }

    @Test
    @DisplayName("hub를 id로 삭제된 데이터는 조회되지 않는다.")
    public void findById_NotDeleted_success() {
        Hub deletedHub = createHub();
        deletedHub.markDeleted(1L);

        testEntityManager.persistAndFlush(deletedHub);

        Optional<Hub> findHub = hubRepository.findById(deletedHub.getHubId());

        assertThat(findHub.isEmpty()).isTrue();
    }

    private static Hub createHub() {
        return Hub.builder()
                .name("송파 허브")
                .address(Address.of("street", Coordinate.of(12.6, 12.6)))
                .build();
    }
}