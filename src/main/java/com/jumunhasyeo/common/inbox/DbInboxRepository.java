package com.jumunhasyeo.common.inbox;

import org.springframework.stereotype.Repository;

@Repository
public interface DbInboxRepository {
    void save(InboxEvent inboxEvent);
}
