package com.jumunhasyeo.hub.hubRoute.infrastructure.repository;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hubRoute.domain.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaHubRouteRepositoryImpl extends JpaRepository<HubRoute, UUID> {

    @Query("SELECT hr FROM HubRoute hr WHERE (hr.startHub = :startHub OR hr.endHub = :endHub) AND hr.isDeleted = false")
    List<HubRoute> findByStartHubOrEndHub(
            @Param("startHub") Hub startHub, 
            @Param("endHub") Hub endHub
    );
    
    @Query("SELECT hr FROM HubRoute hr WHERE hr.isDeleted = false")
    List<HubRoute> findAll();
}
