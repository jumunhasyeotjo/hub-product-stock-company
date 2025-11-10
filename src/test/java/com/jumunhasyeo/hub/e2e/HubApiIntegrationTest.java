package com.jumunhasyeo.hub.e2e;

import com.jumunhasyeo.CleanUp;
import com.jumunhasyeo.CommonTestContainer;
import com.jumunhasyeo.TestConfig;
import com.jumunhasyeo.hub.exception.ErrorCode;
import com.jumunhasyeo.hub.presentation.dto.request.CreateHubReq;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import({CleanUp.class, TestConfig.class})
public class HubApiIntegrationTest extends CommonTestContainer {

    @LocalServerPort
    private int port;

    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.truncateAll();
        RestAssured.port = port;
    }

    @Test
    @DisplayName("허브 생성부터 조회까지 전체 플로우")
    void createAndGetHub_EndToEndFlow() {
        // given
        CreateHubReq request = new CreateHubReq("이름", "서울시 송파구 허브", 12.6, 12.6);

        // when: 허브 생성
        String hubId = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/hubs")
                .then()
                .statusCode(201)
                .extract().path("data.id");

        // then: 생성된 허브 조회
        given()
                .when()
                .get("/api/v1/hubs/{hubId}", hubId)
                .then()
                .statusCode(200)
                .body("data.id", equalTo(hubId));
    }

    @Test
    @DisplayName("허브 생성시 중복된 이름일 경우 예외 반환")
    void createDuplicateName_shouldThrowException() {
        // given
        CreateHubReq request = new CreateHubReq("이름", "서울시 송파구 허브", 12.6, 12.6);

        // when: 허브 2개 생성
        String hubId = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/hubs")
                .then()
                .statusCode(201)
                .extract().path("data.id");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/hubs")
                .then()
                .statusCode(400)
                .body("code", equalTo(ErrorCode.ALREADY_EXISTS.name()))
                .body("message", containsString("이미 존재하는 허브 이름입니다."));

    }
}
