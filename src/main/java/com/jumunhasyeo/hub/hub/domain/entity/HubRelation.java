package com.jumunhasyeo.hub.hub.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_hub_relation",
        uniqueConstraints = @UniqueConstraint(
        columnNames = {"general_hub_id", "middle_hub_id"}
))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HubRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_relation_id", columnDefinition = "UUID")
    private UUID HubRelationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "middle_hub_id")
    private Hub centerHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_hub_id")
    private Hub branchHub;

    private HubRelation(Hub branchHub, Hub centerHub) {
        this.branchHub = branchHub;
        this.centerHub = centerHub;
    }

    static HubRelation of(Hub branchHub, Hub centerHub) {
        return new HubRelation(branchHub, centerHub);
    }
}
