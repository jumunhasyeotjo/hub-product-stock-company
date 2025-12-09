package com.jumunhasyeo.common.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDbInboxRepository extends JpaRepository<InboxEvent, UUID> {
}
