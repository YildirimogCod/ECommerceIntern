package com.yildirimog.ecommercestaj.payment.mapper;

import com.yildirimog.ecommercestaj.payment.dto.PaymentResponse;
import com.yildirimog.ecommercestaj.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    PaymentResponse toResponse(Payment payment);
}
