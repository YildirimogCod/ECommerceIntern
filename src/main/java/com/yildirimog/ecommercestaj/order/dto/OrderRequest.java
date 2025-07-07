package com.yildirimog.ecommercestaj.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "User ID cannot be null")
       Long userId,
        @NotEmpty(message = "Order items cannot be null")
       List<OrderItemRequest> orderItems
) {
}
