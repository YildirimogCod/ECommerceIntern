package com.yildirimog.ecommercestaj.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@RedisHash("cart") // Redis hash for storing cart data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    private String id; // userId bazlı tutulabilir: "cart:user:15"

    private List<CartItem> items = new ArrayList<>();
}
