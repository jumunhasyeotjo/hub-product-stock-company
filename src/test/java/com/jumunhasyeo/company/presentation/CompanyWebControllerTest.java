package com.jumunhasyeo.company.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.ControllerTestConfig;
import com.jumunhasyeo.common.exception.GlobalExceptionHandler;
import com.jumunhasyeo.company.application.CompanyService;
import com.jumunhasyeo.company.application.dto.response.CompanyRes;
import com.jumunhasyeo.company.domain.entity.CompanyType;
import com.jumunhasyeo.company.presentation.dto.request.CreateCompanyReq;
import com.jumunhasyeo.company.presentation.dto.request.DeleteCompanyReq;
import com.jumunhasyeo.company.presentation.dto.request.UpdateCompanyReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyWebController.class)
@Import({ControllerTestConfig.class, GlobalExceptionHandler.class})
class CompanyWebControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CompanyService companyService;

    @Test
    @DisplayName("업체를 생성할 수 있다.")
    void create_success() throws Exception {
        //given
        UUID hubId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        CreateCompanyReq req = new CreateCompanyReq(hubId, "테스트업체", CompanyType.PRODUCER, "서울시");
        CompanyRes res = new CompanyRes(companyId, hubId, "테스트업체", CompanyType.PRODUCER, "서울시");
        given(companyService.create(any())).willReturn(res);

        //when & then
        mockMvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.companyId").value(companyId.toString()))
                .andExpect(jsonPath("$.data.name").value("테스트업체"));
    }

    @Test
    @DisplayName("업체를 단건 조회할 수 있다.")
    void getById_success() throws Exception {
        //given
        UUID companyId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();
        CompanyRes res = new CompanyRes(companyId, hubId, "테스트업체", CompanyType.PRODUCER, "서울시");
        given(companyService.getById(companyId)).willReturn(res);

        //when & then
        mockMvc.perform(get("/api/v1/companies/{companyId}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.companyId").value(companyId.toString()))
                .andExpect(jsonPath("$.data.name").value("테스트업체"));
    }

    @Test
    @DisplayName("업체 전체를 조회할 수 있다.")
    void getAll_success() throws Exception {
        //given
        List<CompanyRes> resList = List.of(
                new CompanyRes(UUID.randomUUID(), UUID.randomUUID(), "업체1", CompanyType.PRODUCER, "서울"),
                new CompanyRes(UUID.randomUUID(), UUID.randomUUID(), "업체2", CompanyType.RECEIVER, "부산")
        );
        given(companyService.getAll()).willReturn(resList);

        //when & then
        mockMvc.perform(get("/api/v1/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("업체를 수정할 수 있다.")
    void update_success() throws Exception {
        //given
        UUID companyId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();
        UpdateCompanyReq req = new UpdateCompanyReq(companyId, hubId, "수정업체", CompanyType.RECEIVER, "부산시");
        CompanyRes res = new CompanyRes(companyId, hubId, "수정업체", CompanyType.RECEIVER, "부산시");
        given(companyService.update(any())).willReturn(res);

        //when & then
        mockMvc.perform(patch("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("수정업체"));
    }

    @Test
    @DisplayName("업체를 삭제할 수 있다.")
    void delete_success() throws Exception {
        //given
        UUID companyId = UUID.randomUUID();
        DeleteCompanyReq req = new DeleteCompanyReq(companyId, 1L);
        given(companyService.delete(any())).willReturn(companyId);

        //when & then
        mockMvc.perform(delete("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(companyId.toString()));
    }

    @Test
    @DisplayName("업체 존재 여부를 검증할 수 있다 - 존재하는 경우")
    void exists_true() throws Exception {
        //given
        UUID companyId = UUID.randomUUID();
        given(companyService.existsById(companyId)).willReturn(true);

        //when & then
        mockMvc.perform(get("/api/v1/companies/{companyId}/exists", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @DisplayName("업체 존재 여부를 검증할 수 있다 - 존재하지 않는 경우")
    void exists_false() throws Exception {
        //given
        UUID companyId = UUID.randomUUID();
        given(companyService.existsById(companyId)).willReturn(false);

        //when & then
        mockMvc.perform(get("/api/v1/companies/{companyId}/exists", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    @DisplayName("업체 소속 허브를 검증할 수 있다 - 소속인 경우")
    void belongsToHub_true() throws Exception {
        //given
        UUID companyId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();
        given(companyService.existsByIdAndHubId(companyId, hubId)).willReturn(true);

        //when & then
        mockMvc.perform(get("/api/v1/companies/{companyId}/hub/{hubId}/belongs", companyId, hubId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @DisplayName("업체 소속 허브를 검증할 수 있다 - 소속이 아닌 경우")
    void belongsToHub_false() throws Exception {
        //given
        UUID companyId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();
        given(companyService.existsByIdAndHubId(companyId, hubId)).willReturn(false);

        //when & then
        mockMvc.perform(get("/api/v1/companies/{companyId}/hub/{hubId}/belongs", companyId, hubId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }
}
