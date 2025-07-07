package com.yildirimog.ecommercestaj.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        List<OrderItemResponse> items,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
