package com.yildirimog.ecommercestaj.product.repository;

import com.yildirimog.ecommercestaj.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
