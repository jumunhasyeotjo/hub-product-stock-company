package com.jumunhasyeo.company.infrastructure.repository;

import com.jumunhasyeo.company.domain.entity.Company;
import com.jumunhasyeo.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {
    private final JpaCompanyRepository jpaCompanyRepository;

    @Override
    public Company save(Company company) {
        return jpaCompanyRepository.save(company);
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return jpaCompanyRepository.findById(id);
    }

    @Override
    public List<Company> findAll() {
        return jpaCompanyRepository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaCompanyRepository.existsById(id);
    }

    @Override
    public boolean existsByIdAndHubId(UUID companyId, UUID hubId) {
        return jpaCompanyRepository.existsByIdAndHubId(companyId, hubId);
    }
}
