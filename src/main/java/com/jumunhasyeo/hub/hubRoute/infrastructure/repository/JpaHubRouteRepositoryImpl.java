package com.jumunhasyeo.hub.hubRoute.infrastructure.repository;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hubRoute.domain.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = """
                INSERT INTO p_hub_route(route_id, start_hub_id, end_hub_id, distance_km, duration_minutes, created_at, modified_at, deleted_at, is_deleted)
                VALUES (gen_random_uuid(), :startId, :endId, :distanceKm, :durationMinutes, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, false)
                ON CONFLICT (start_hub_id, end_hub_id, is_deleted) 
                DO NOTHING;
            """, nativeQuery = true)
    void insertIgnore(
                       @Param("startId") UUID startId,
                       @Param("endId") UUID endId,
                       @Param("distanceKm") double distanceKm,
                       @Param("durationMinutes") int durationMinutes
    );
}
