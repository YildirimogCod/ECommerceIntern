package com.yildirimog.ecommercestaj.cart.controller;

import com.yildirimog.ecommercestaj.cart.dto.CartResponse;
import com.yildirimog.ecommercestaj.cart.entity.Cart;
import com.yildirimog.ecommercestaj.cart.mapper.CartMapper;
import com.yildirimog.ecommercestaj.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
        CartResponse cartResponse = cartService.getCartResponseByUserId(userId);
        return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable String userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        Cart cart = cartService.addItemToCart(userId, productId, quantity);
        CartResponse response = cartMapper.toCartResponse(cart);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable String userId,
            @RequestParam Long productId) {
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
