package com.yildirimog.ecommercestaj.wishlist.controller;

import com.yildirimog.ecommercestaj.wishlist.dto.WishlistItemCreateRequest;
import com.yildirimog.ecommercestaj.wishlist.dto.WishlistItemResponse;
import com.yildirimog.ecommercestaj.wishlist.service.WishlistItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistItemController {
    private final WishlistItemService wishlistItemService;
    @PostMapping
    public ResponseEntity<WishlistItemResponse> addToWishlist(@Valid @RequestBody WishlistItemCreateRequest request){
       WishlistItemResponse wishlistItemResponse = wishlistItemService.addToWishlist(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishlistItemResponse);
    }
    @DeleteMapping
    public ResponseEntity<Void> removeFromWishlist(@RequestParam Long userId,
                                                   @RequestParam Long productId) {
        wishlistItemService.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistItemResponse>> getUserWishlist(@PathVariable Long userId) {
        List<WishlistItemResponse> wishlist = wishlistItemService.getUserWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }
}
