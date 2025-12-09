package com.jumunhasyeo.common.outbox;

import com.jumunhasyeo.common.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_outbox_events",
       indexes = {
           @Index(name = "idx_outbox_event_event_key", columnList = "eventKey")
       }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Outbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String eventKey;

    @Type(JsonBinaryType.class)
    @Column(nullable = false, columnDefinition = "JSONB")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private Outbox(String eventName, String payload, OutboxStatus status, String eventKey) {
        this.eventName = eventName;
        this.payload = payload;
        this.status = status;
        this.eventKey = eventKey;
    }

    public static Outbox of(String eventName, String payload, String eventKey) {
        return new Outbox(eventName, payload, OutboxStatus.PENDING, eventKey);
    }

    public void markProcessed() {
        this.status = OutboxStatus.COMPLETE;
    }
}
