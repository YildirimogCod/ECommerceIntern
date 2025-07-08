package com.yildirimog.ecommercestaj.idempotency.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "idempotency_keys")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdempotencyKey {
    @Id
    private String key;
    private Long userId;
    @Lob
    private String responseJson;
    private Instant createdAt;
}
