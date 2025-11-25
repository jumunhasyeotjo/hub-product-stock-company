package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.common.BaseEntity;
import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.domain.vo.Address;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_hub")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id", columnDefinition = "UUID")
    private UUID hubId;

    @Column(name = "name", nullable = false, length = 20, unique = true)
    private String name;

    @Embedded
    private Address address;

    private Hub(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public static Hub of(String name, Address address) {
        validate(name, address);
        return new Hub(name, address);
    }

    private static void validate(String name, Address address) {
        if (StringUtils.isEmpty(name) || address == null) {
            throw new BusinessException(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        }
    }

    public void update(String name, Address address) {
        validate(name, address);
        this.name = name;
        this.address = address;
    }

    public void delete(Long userId) {
        if (userId == null)
            throw new BusinessException(ErrorCode.MUST_NOT_NULL, "userId");
        markDeleted(userId);
    }
}