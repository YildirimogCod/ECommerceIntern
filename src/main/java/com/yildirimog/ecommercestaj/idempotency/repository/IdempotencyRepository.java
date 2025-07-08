package com.yildirimog.ecommercestaj.idempotency.repository;

import com.yildirimog.ecommercestaj.idempotency.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<IdempotencyKey,String> {
    Optional<IdempotencyKey> findByKeyAndUserId(String key,Long userId);
}
