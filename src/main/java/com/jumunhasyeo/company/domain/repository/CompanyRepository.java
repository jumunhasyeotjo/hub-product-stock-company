package com.jumunhasyeo.company.domain.repository;

import com.jumunhasyeo.company.domain.entity.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> findById(UUID id);
    List<Company> findAll();
    boolean existsById(UUID id);
    boolean existsByIdAndHubId(UUID companyId, UUID hubId);
}
