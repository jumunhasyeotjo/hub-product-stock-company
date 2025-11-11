package com.jumunhasyeo;


import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CleanUp {

    private final EntityManager em;

    public CleanUp(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void truncateAll() {
        em.flush();

        // PostgreSQL 메타데이터에서 모든 테이블 조회
        List<String> tableNames = em.createNativeQuery(
                """
                SELECT table_name 
                FROM information_schema.tables 
                WHERE table_schema = 'public' 
                  AND table_type = 'BASE TABLE'
                """, String.class
        ).getResultList();

        // 모든 테이블 TRUNCATE (CASCADE로 FK 무시)
        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE")
                    .executeUpdate();
        }
    }
}
