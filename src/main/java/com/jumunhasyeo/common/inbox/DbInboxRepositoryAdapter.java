package com.jumunhasyeo.common.inbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DbInboxRepositoryAdapter implements DbInboxRepository{
    private final JpaDbInboxRepository jpaDbInboxRepository;

    @Override
    public void save(InboxEvent inboxEvent) {
        jpaDbInboxRepository.save(inboxEvent);
    }
}
