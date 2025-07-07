package com.yildirimog.ecommercestaj.payment.repository;

import com.yildirimog.ecommercestaj.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
