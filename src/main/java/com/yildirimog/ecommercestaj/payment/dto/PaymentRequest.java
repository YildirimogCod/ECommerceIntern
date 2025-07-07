package com.yildirimog.ecommercestaj.payment.dto;

public record PaymentRequest(
        Long orderId,
        String provider
) {
}
