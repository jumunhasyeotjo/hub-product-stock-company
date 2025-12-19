package com.jumunhasyeo.hub.hubRoute.domain.entity;

import com.jumunhasyeo.common.BaseEntity;
import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hubRoute.domain.vo.RouteWeight;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.UUID;

@Entity
@Table(
        name = "p_hub_route",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_start_end_hub_deleted_at",
                columnNames = {"start_hub_id", "end_hub_id", "is_deleted"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HubRoute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "route_id")
    private UUID routeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "start_hub_id", nullable = false)
    private Hub startHub;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "end_hub_id", nullable = false)
    private Hub endHub;

    @Embedded
    private RouteWeight routeWeight;

    public static HubRoute of(Hub startHub, Hub endHub, RouteWeight weight) {
        return HubRoute.builder()
                .startHub(startHub)
                .endHub(endHub)
                .routeWeight(weight)
                .build();
    }

    public static HubRoute ofSelfId(UUID routeId, Hub startHub, Hub endHub, RouteWeight weight) {
        return HubRoute.builder()
                .routeId(routeId)
                .startHub(startHub)
                .endHub(endHub)
                .routeWeight(weight)
                .build();
    }

    public static HashSet<HubRoute> createTwoWay(Hub from, Hub to, RouteWeight routeWeight) {
        HashSet<HubRoute> hubRoutes = new HashSet<>();
        hubRoutes.add(ofSelfId(UUID.randomUUID(), from, to, routeWeight));
        hubRoutes.add(ofSelfId(UUID.randomUUID(), to, from, routeWeight));
        return hubRoutes;
    }
}
