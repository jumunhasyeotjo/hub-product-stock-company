package com.jumunhasyeo.hub.hub.infrastructure.repository;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hub.domain.entity.HubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

    @Query("SELECT h FROM Hub h WHERE h.hubId = :id AND h.isDeleted = false")
    Optional<Hub> findById(@Param("id") UUID id);

    @Query("SELECT h FROM Hub h WHERE h.hubType = :hubType AND h.isDeleted = false")
    List<Hub> findAllByHubType(@Param("hubType") HubType hubType);
    
    @Query("SELECT h FROM Hub h WHERE h.isDeleted = false")
    List<Hub> findAll();
    
    @Query("SELECT COUNT(h) FROM Hub h WHERE h.isDeleted = false")
    long count();
    
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Hub h WHERE h.hubId = :id AND h.isDeleted = false")
    boolean existsById(@Param("id") UUID id);
}
