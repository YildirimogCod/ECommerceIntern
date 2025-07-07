package com.yildirimog.ecommercestaj.cart.mapper;

import com.yildirimog.ecommercestaj.cart.dto.CartItemResponse;
import com.yildirimog.ecommercestaj.cart.dto.CartResponse;
import com.yildirimog.ecommercestaj.cart.entity.Cart;
import com.yildirimog.ecommercestaj.cart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {
    public CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toCartItemResponse)
                .toList();

        BigDecimal cartTotal = items.stream()
                .map(CartItemResponse::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), items, cartTotal);
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        BigDecimal totalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                totalPrice
        );
    }
}
