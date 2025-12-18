package com.jumunhasyeo.company.infrastructure.repository;

import com.jumunhasyeo.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT c FROM Company c WHERE c.companyId = :id AND c.isDeleted = false")
    Optional<Company> findById(@Param("id") UUID id);

    @Query("SELECT c FROM Company c WHERE c.isDeleted = false")
    List<Company> findAll();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE c.companyId = :id AND c.isDeleted = false")
    boolean existsById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE c.companyId = :companyId AND c.hubId = :hubId AND c.isDeleted = false")
    boolean existsByIdAndHubId(@Param("companyId") UUID companyId, @Param("hubId") UUID hubId);
}
