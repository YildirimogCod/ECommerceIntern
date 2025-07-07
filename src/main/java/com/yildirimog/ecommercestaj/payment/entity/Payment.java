package com.yildirimog.ecommercestaj.payment.entity;

import com.yildirimog.ecommercestaj.common.BaseEntity;
import com.yildirimog.ecommercestaj.common.enums.PaymentStatus;
import com.yildirimog.ecommercestaj.order.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionId;
    private String provider;
    private LocalDateTime paidAt;
}
