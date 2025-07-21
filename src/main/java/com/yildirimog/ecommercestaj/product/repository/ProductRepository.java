package com.yildirimog.ecommercestaj.product.repository;

import com.yildirimog.ecommercestaj.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Modifying
    @Transactional
    @Query("""
    UPDATE Product p SET
        p.name = :name,
        p.description = :description,
        p.price = :price,
        p.stockQuantity = :stockQuantity,
        p.imageUrl = :imageUrl
    WHERE p.id = :id
""")
    int updateProductWithoutCategory(@Param("id") Long id,
                                     @Param("name") String name,
                                     @Param("description") String description,
                                     @Param("price") BigDecimal price,
                                     @Param("stockQuantity") Integer stockQuantity,
                                     @Param("imageUrl") String imageUrl);

}
