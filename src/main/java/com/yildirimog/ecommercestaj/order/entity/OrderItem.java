package com.yildirimog.ecommercestaj.order.entity;

import com.yildirimog.ecommercestaj.common.BaseEntity;
import com.yildirimog.ecommercestaj.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;
}
