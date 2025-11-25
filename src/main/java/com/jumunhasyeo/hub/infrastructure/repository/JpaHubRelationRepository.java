package com.jumunhasyeo.hub.infrastructure.repository;

import com.jumunhasyeo.hub.domain.entity.HubRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHubRelationRepository extends JpaRepository<HubRelation, UUID> {
}
