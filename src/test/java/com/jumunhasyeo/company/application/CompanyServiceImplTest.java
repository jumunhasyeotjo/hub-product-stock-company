package com.jumunhasyeo.company.application;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.company.application.command.CreateCompanyCommand;
import com.jumunhasyeo.company.application.command.DeleteCompanyCommand;
import com.jumunhasyeo.company.application.command.UpdateCompanyCommand;
import com.jumunhasyeo.company.application.dto.response.CompanyRes;
import com.jumunhasyeo.company.domain.entity.Company;
import com.jumunhasyeo.company.domain.entity.CompanyType;
import com.jumunhasyeo.company.domain.repository.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    @DisplayName("업체를 생성할 수 있다.")
    void create_success() {
        //given
        UUID hubId = UUID.randomUUID();
        CreateCompanyCommand command = new CreateCompanyCommand(hubId, "테스트업체", CompanyType.PRODUCER, "서울시");

        //when
        CompanyRes result = companyService.create(command);

        //then
        assertThat(result.name()).isEqualTo("테스트업체");
        assertThat(result.hubId()).isEqualTo(hubId);
    }

    @Test
    @DisplayName("업체를 수정할 수 있다.")
    void update_success() {
        //given
        UUID companyId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();
        Company company = createCompany(companyId, hubId);
        UpdateCompanyCommand command = new UpdateCompanyCommand(companyId, hubId, "수정업체", CompanyType.RECEIVER, "부산시");
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        //when
        CompanyRes result = companyService.update(command);

        //then
        assertThat(result.name()).isEqualTo("수정업체");
        assertThat(result.companyType()).isEqualTo(CompanyType.RECEIVER);
    }

    @Test
    @DisplayName("업체 수정 시 존재하지 않으면 예외가 발생한다.")
    void update_notFound_throwsException() {
        //given
        UUID companyId = UUID.randomUUID();
        UpdateCompanyCommand command = new UpdateCompanyCommand(companyId, UUID.randomUUID(), "수정", CompanyType.PRODUCER, "주소");
        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> companyService.update(command));

        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION);
    }

    @Test
    @DisplayName("업체를 삭제할 수 있다.")
    void delete_success() {
        //given
        UUID companyId = UUID.randomUUID();
        Company company = createCompany(companyId, UUID.randomUUID());
        DeleteCompanyCommand command = new DeleteCompanyCommand(companyId, 1L);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        //when
        UUID result = companyService.delete(command);

        //then
        assertThat(result).isEqualTo(companyId);
    }

    @Test
    @DisplayName("업체를 단건 조회할 수 있다.")
    void getById_success() {
        //given
        UUID companyId = UUID.randomUUID();
        Company company = createCompany(companyId, UUID.randomUUID());
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        //when
        CompanyRes result = companyService.getById(companyId);

        //then
        assertThat(result.companyId()).isEqualTo(companyId);
    }

    @Test
    @DisplayName("업체 전체를 조회할 수 있다.")
    void getAll_success() {
        //given
        List<Company> companies = List.of(
                createCompany(UUID.randomUUID(), UUID.randomUUID()),
                createCompany(UUID.randomUUID(), UUID.randomUUID())
        );
        when(companyRepository.findAll()).thenReturn(companies);

        //when
        List<CompanyRes> result = companyService.getAll();

        //then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("업체 존재 여부를 확인할 수 있다.")
    void existsById_success() {
        //given
        UUID companyId = UUID.randomUUID();
        when(companyRepository.existsById(companyId)).thenReturn(true);

        //when
        boolean result = companyService.existsById(companyId);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("업체 소속 허브 여부를 확인할 수 있다.")
    void existsByIdAndHubId_success() {
        //given
        UUID companyId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();
        when(companyRepository.existsByIdAndHubId(companyId, hubId)).thenReturn(true);

        //when
        boolean result = companyService.existsByIdAndHubId(companyId, hubId);

        //then
        assertThat(result).isTrue();
    }

    private Company createCompany(UUID companyId, UUID hubId) {
        return Company.builder()
                .companyId(companyId)
                .hubId(hubId)
                .name("테스트업체")
                .companyType(CompanyType.PRODUCER)
                .address("서울시")
                .build();
    }
}
