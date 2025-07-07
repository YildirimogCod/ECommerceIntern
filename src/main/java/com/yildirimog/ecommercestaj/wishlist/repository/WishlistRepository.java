package com.yildirimog.ecommercestaj.wishlist.repository;

import com.yildirimog.ecommercestaj.product.entity.Product;
import com.yildirimog.ecommercestaj.user.entity.User;
import com.yildirimog.ecommercestaj.wishlist.entity.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishListItem,Long> {
    boolean existsByUserAndProduct(User user, Product product);
    Optional<WishListItem> findByUserAndProduct(User user, Product product);

    List<WishListItem> findAllByUser(User user);
}
