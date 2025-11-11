package com.jumunhasyeo.hub.infrastructure.repository;

import com.jumunhasyeo.hub.domain.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

    @Query("SELECT h FROM Hub h WHERE h.hubId = :id AND h.deletedAt IS NULL")
    Optional<Hub> findById(@Param("id") UUID id);
}
