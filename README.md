# README.md
<img width="1139" height="374" alt="스크린샷 2025-11-10 오후 4 05 45" src="https://github.com/user-attachments/assets/37f8838e-eee2-4593-8c02-810c442acee3" />

[테스트 가이드](./TEST_GUIDE.md)

[회고](./회고.md)
## TDD 학습 과정
TDD 세션에서 튜터님께서 제공해주신 강의와 강의 자료를 통해 기본적인 지식을 습득했습니다. <br/>
그리고 TDD에 대한 다양한 관점에서의 토론을 통해 시각과 TDD의 필요성에 대해 깨닫는 시간을 가졌습니다. <br/>

## Step별 학습 내용
### Step 1: TDD 기초와 도메인 모델 테스트
- TDD란 무엇인가? (Red-Green-Refactor)
- 도메인 주도 설계에서 도메인 모델의 역할
- 단위 테스트 작성 기본 (JUnit 5, AssertJ)
- Given-When-Then 패턴

회고
- 익숙치 않은 개발 프로세스를 거치며 TDD와 DDD가 결합되었을 때 나올 수 있는 시너지를 느껴볼 수 있었습니다.

### Step 2: 서비스 레이어 테스트와 Mock 활용
- 서비스 레이어의 책임과 테스트 범위
- Mockito를 활용한 의존성 격리
- @Mock, @InjectMocks, verify() 사용법
- 테스트 더블(Test Double) 개념

회고
- 테스트 간에도 각 책임을 분리해야 한다는 것과 테스트 더블의 다양성과 사용 의미와 시기에 대해서 알게 되었습니다.

### Step 3: Repository 테스트
- @DataJpaTest를 활용한 Repository 테스트
- TestContainers 또는 H2 In-Memory DB 활용
- QueryDSL 기반 동적 쿼리 테스트
- 테스트 데이터 격리 전략

회고
- DataJpaTest를 통해 Repository 테스트를 진행하며 영속성 전이와 트랜잭션에 대해서 조금 더 체감할 수 있는 시간이 될 수 있었다고 생각합니다.

### Step 4: API 테스트와 통합 테스트
- @WebMvcTest를 활용한 Controller 단위 테스트
  → DispatcherServlet, Controller, ExceptionHandler 등 **Spring MVC 관련 빈만 로드**
- MockMvc를 활용한 HTTP 요청/응답 테스트
  → Spring MVC의 DispatcherServlet을 통해 요청을 수행할 수 있는 객체
- RestAssured를 활용한 E2E API 테스트
- @SpringBootTest를 활용한 전체 통합 테스트

회고
- 이 또한 Controller 테스트의 책임 범위에 대해서 배웠습니다.
- E2E 테스트라는 통합 테스트 개념에 대해서 처음으로 배우게 됐고 생각한 것만큼 어렵지는 않고 그동안 멀게 생각해왔다는 것을 느꼈습니다.

### Step 5: 복잡한 비즈니스 로직과 리팩토링
- 복잡한 비즈니스 규칙을 테스트로 표현하기
- 파라미터화된 테스트 (@ParameterizedTest)
- 테스트 Fixture 공통화
- 테스트가 보장하는 안전한 리팩토링

회고
- 리팩토링을 거쳐가며 메서드의 가독성과 코드 중복을 배제하고 책임 분리를 하게 됐습니다. 이 과정에서 특히나 TDD의 존재 의의를 체감한 것 같습니다.

## Test 전략
DDD와 TDD 설계를 동시에 진행함으로 도메인의 비즈니스 로직 위주의 테스트를 진행하였습니다. <br/>
그로 하여금 Layer 계층이 올라갈 수록 테스트의 범위와 수가 줄어들 수 있도록 계획했습니다. <br/>
