package com.yildirimog.ecommercestaj.payment.service;

import com.yildirimog.ecommercestaj.common.enums.OrderStatus;
import com.yildirimog.ecommercestaj.common.enums.PaymentStatus;
import com.yildirimog.ecommercestaj.order.entity.Order;
import com.yildirimog.ecommercestaj.order.repository.OrderRepository;
import com.yildirimog.ecommercestaj.payment.dto.PaymentRequest;
import com.yildirimog.ecommercestaj.payment.dto.PaymentResponse;
import com.yildirimog.ecommercestaj.payment.entity.Payment;
import com.yildirimog.ecommercestaj.payment.mapper.PaymentMapper;
import com.yildirimog.ecommercestaj.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponse makePayment(PaymentRequest request){
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + request.orderId()));
        if (order.getStatus()== OrderStatus.PAID) {
            throw new IllegalStateException("Order is already paid");
        }
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setProvider(request.provider());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        return paymentMapper.toResponse(payment);
    }
}
