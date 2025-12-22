package com.jumunhasyeo.company.application;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.company.application.command.CreateCompanyCommand;
import com.jumunhasyeo.company.application.command.DeleteCompanyCommand;
import com.jumunhasyeo.company.application.command.UpdateCompanyCommand;
import com.jumunhasyeo.company.application.dto.response.CompanyRes;
import com.jumunhasyeo.company.domain.entity.Company;
import com.jumunhasyeo.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyRes create(CreateCompanyCommand command) {
        Company company = Company.of(
                command.hubId(),
                command.name(),
                command.companyType(),
                command.address()
        );
        companyRepository.save(company);
        return CompanyRes.from(company);
    }

    @Override
    @Transactional
    public CompanyRes update(UpdateCompanyCommand command) {
        Company company = getCompany(command.companyId());
        company.update(command.hubId(), command.name(), command.companyType(), command.address());
        return CompanyRes.from(company);
    }

    @Override
    @Transactional
    public UUID delete(DeleteCompanyCommand command) {
        Company company = getCompany(command.companyId());
        company.delete(command.userId());
        return company.getCompanyId();
    }

    @Override
    public CompanyRes getById(UUID companyId) {
        return CompanyRes.from(getCompany(companyId));
    }

    @Override
    public List<CompanyRes> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyRes::from)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID companyId) {
        return companyRepository.existsById(companyId);
    }

    @Override
    public boolean existsByIdAndHubId(UUID companyId, UUID hubId) {
        return companyRepository.existsByIdAndHubId(companyId, hubId);
    }

    private Company getCompany(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION, "companyId = "+companyId));
    }
}
