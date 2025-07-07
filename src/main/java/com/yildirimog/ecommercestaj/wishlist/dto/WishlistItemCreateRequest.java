package com.yildirimog.ecommercestaj.wishlist.dto;

public record WishlistItemCreateRequest(
        Long userId,
        Long productId
) {
}
