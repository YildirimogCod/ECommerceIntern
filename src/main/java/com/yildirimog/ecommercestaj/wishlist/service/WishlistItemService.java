package com.yildirimog.ecommercestaj.wishlist.service;

import com.yildirimog.ecommercestaj.product.entity.Product;
import com.yildirimog.ecommercestaj.product.repository.ProductRepository;
import com.yildirimog.ecommercestaj.user.entity.User;
import com.yildirimog.ecommercestaj.user.repository.UserRepository;
import com.yildirimog.ecommercestaj.wishlist.dto.WishlistItemCreateRequest;
import com.yildirimog.ecommercestaj.wishlist.dto.WishlistItemResponse;
import com.yildirimog.ecommercestaj.wishlist.entity.WishListItem;
import com.yildirimog.ecommercestaj.wishlist.mapper.WishlistMapper;
import com.yildirimog.ecommercestaj.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistItemService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistMapper wishlistMapper;

    public WishlistItemResponse addToWishlist(WishlistItemCreateRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if(wishlistRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("This product is already in your wishlist");
        }
        WishListItem wishlistItem = WishListItem
                .builder()
                .user(user)
                .product(product)
                .addedAt(LocalDateTime.now())
                .build();
        WishListItem savedItem = wishlistRepository.save(wishlistItem);
        return wishlistMapper.toResponse(savedItem);
    }
    public void removeFromWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        WishListItem wishlistItem = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));
        wishlistRepository.delete(wishlistItem);
    }
    public List<WishlistItemResponse> getUserWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        List<WishListItem> items = wishlistRepository.findAllByUser(user);

        return items.stream()
                .map(wishlistMapper::toResponse)
                .toList();
    }

    public void deleteWishlistItem(Long id) {
        WishListItem wishlistItem = wishlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));
        wishlistRepository.delete(wishlistItem);
    }
}
