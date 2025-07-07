package com.yildirimog.ecommercestaj.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        String id,
        List<CartItemResponse> items,
        BigDecimal cartTotal
) {
}
