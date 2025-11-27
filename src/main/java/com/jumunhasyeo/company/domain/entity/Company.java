package com.jumunhasyeo.company.domain.entity;

import com.jumunhasyeo.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "hub_id", nullable = false, columnDefinition = "UUID")
    private UUID hubId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;   // 생산 / 수령

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    private Company(UUID hubId, String name, CompanyType companyType, String address) {
        this.hubId = hubId;
        this.name = name;
        this.companyType = companyType;
        this.address = address;
    }

    public static Company of(UUID hubId, String name, CompanyType companyType, String address) {
        return new Company(hubId, name, companyType, address);
    }

    public void update(UUID hubId, String name, CompanyType companyType, String address) {
        this.hubId = hubId;
        this.name = name;
        this.companyType = companyType;
        this.address = address;
    }

    public void delete(Long userId) {
        markDeleted(userId);
    }
}
