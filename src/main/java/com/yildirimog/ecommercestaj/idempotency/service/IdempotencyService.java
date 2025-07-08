package com.yildirimog.ecommercestaj.idempotency.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yildirimog.ecommercestaj.idempotency.entity.IdempotencyKey;
import com.yildirimog.ecommercestaj.idempotency.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final ObjectMapper objectMapper;
    private final IdempotencyRepository idempotencyRepository;
    public <T>Optional<T> checkAndGet(String key,Long userId, Class<T> responseType){
        return idempotencyRepository.findByKeyAndUserId(key, userId)
                .map(idempotencyKey -> {
                    try {
                        return Optional.of(objectMapper.readValue(idempotencyKey.getResponseJson(), responseType));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse response JSON", e);
                    }
                })
                .orElse(Optional.empty());
    }
    public void save(String key, Long userId, Object response) {
        try {
            String json = objectMapper.writeValueAsString(response);
            IdempotencyKey entity = IdempotencyKey.builder()
                    .key(key)
                    .userId(userId)
                    .responseJson(json)
                    .createdAt(Instant.now())
                    .build();
            idempotencyRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save idempotent response", e);
        }
    }
}
