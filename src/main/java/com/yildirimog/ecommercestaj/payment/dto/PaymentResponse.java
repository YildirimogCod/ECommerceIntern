package com.yildirimog.ecommercestaj.payment.dto;

import com.yildirimog.ecommercestaj.common.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        String provider,
        String transactionId,
        PaymentStatus status,
        LocalDateTime paidAt
) {
}
