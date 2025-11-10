package com.jumunhasyeo.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;

    @LastModifiedBy
    @Column
    private Long modifiedBy;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private Long deletedBy;

    public void markDeleted(Long userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

    public void cancelDeleted(Long userId) {
        this.deletedAt = null;
        this.deletedBy = userId;
    }
}
