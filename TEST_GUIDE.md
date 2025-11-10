# 테스트 작성 가이드

## 📌 테스트 네이밍 규칙

### 네이밍 컨벤션
- **성공 케이스**: `{method}_{entity}_success`
- **예외 케이스**: `{method}_{condition}_ShouldThrowException`
---

## 🎨 @DisplayName 작성 규칙

### 한글 사용 원칙
```java
@DisplayName("허브를 생성할 수 있다.")
@DisplayName("허브 이름이 null일 경우 예외가 발생한다.")
@DisplayName("재고 수량이 음수일 때 예외를 반환한다.")
```

### 패턴
- **기능 테스트**: "~할 수 있다"
- **예외 테스트**: "~일 경우 예외가 발생한다/예외를 반환한다"
---

## 🏗 Fixture 사용법
전역 공통 Fixture 대신 Test 파일 단위로 재사용하는 Fixture를 이용했습니다.  
### 1. 테스트 픽스처 패턴
```java
// HubServiceTest.java
private Hub createHub() {
    return Hub.builder()
            .name("송파 허브")
            .address(Address.of("street", Coordinate.of(12.6, 12.6)))
            .stockList(new ArrayList<>())
            .build();
}

// 사용
@Test
void test() {
    Hub hub = createHub();
    // ...
}
```

### 2. 파라미터가 있는 Fixture
```java
private Hub createHub(UUID hubId) {
    return Hub.builder()
            .hubId(hubId)
            .name("송파 허브")
            .address(Address.of("street", Coordinate.of(12.6, 12.6)))
            .build();
}

// 사용
@Test
void test() {
    UUID hubId = UUID.randomUUID();
    Hub hub = createHub(hubId);
    // ...
}
```


---

## 🎭 Mock 사용 가이드

### 1. Mockito 기본 패턴
```java
@ExtendWith(MockitoExtension.class)
class HubServiceTest {
    
    @Mock
    private HubRepository hubRepository;
    
    @InjectMocks
    private HubService hubService;
    
    @Test
    void test() {
        // given - Mock 설정
        Hub hub = createHub(UUID.randomUUID());
        when(hubRepository.findById(any())).thenReturn(Optional.of(hub));
        
        // when - 실행
        HubRes result = hubService.getById(hub.getHubId());
        
        // then - 검증
        assertThat(result.id()).isEqualTo(hub.getHubId());
    }
}

```

### 3. ArgumentCaptor 사용
```java
@Test
void createHub_ShouldPublishEvent() {
    // given
    CreateHubCommand command = new CreateHubCommand("이름", "주소", 12.7, 12.7);
    
    // when
    hubService.create(command);
    
    // then
    ArgumentCaptor<HubCreatedEvent> captor = 
        ArgumentCaptor.forClass(HubCreatedEvent.class);
    verify(eventPublisher).publishEvent(captor.capture());
    
    HubCreatedEvent event = captor.getValue();
    assertThat(event.getHubId()).isNotNull();
}
```

### 4. 예외 Mocking
```java
@Test
void test_Exception() {
    // given
    given(hubRepository.findById(any()))
        .willThrow(new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION));
    
    // when & then
    assertThatThrownBy(() -> hubService.getById(UUID.randomUUID()))
        .isInstanceOf(BusinessException.class)
        .hasMessageContaining("조회에 실패했습니다");
}
```

---

## 🧪 테스트 격리 전략

### 1. @DataJpaTest - Repository 테스트
```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslTestConfig.class, CleanUp.class})
class RepositoryTest extends CommonTestContainer {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @BeforeEach
    void setUp() {
        cleanUp.truncateAll();
        // 테스트 데이터 준비
    }
}
```

### 2. @WebMvcTest - Controller 테스트
```java
@WebMvcTest(HubWebController.class)
@Import({ControllerTestConfig.class, GlobalExceptionHandler.class})
class ControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private HubService hubService;
}
```

### 3. @SpringBootTest - E2E 테스트
```java
@SpringBootTest
class HubIntegrationTest extends CommonTestContainer {
    
    @Autowired
    private HubService hubService;
    
    @Autowired
    private HubRepository hubRepository;

    //cleanUp
}
```

---
