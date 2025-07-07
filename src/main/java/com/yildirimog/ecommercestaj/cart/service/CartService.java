package com.yildirimog.ecommercestaj.cart.service;

import com.yildirimog.ecommercestaj.cart.dto.CartResponse;
import com.yildirimog.ecommercestaj.cart.entity.Cart;
import com.yildirimog.ecommercestaj.cart.entity.CartItem;
import com.yildirimog.ecommercestaj.cart.mapper.CartMapper;
import com.yildirimog.ecommercestaj.cart.repository.CartRepository;
import com.yildirimog.ecommercestaj.product.entity.Product;
import com.yildirimog.ecommercestaj.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    public Cart getCartByUserId(String userId) {
        return cartRepository.findById("cart:user:" + userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .id("cart:user:" + userId)
                            .build();
                    return cartRepository.save(newCart);
                });
    }
    public CartResponse getCartResponseByUserId(String userId) {
        Cart cart = getCartByUserId(userId);
        return cartMapper.toCartResponse(cart);
    }


        public Cart addItemToCart(String userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        // Sepette zaten varsa quantity güncelle
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }
    public void removeItemFromCart(String userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
