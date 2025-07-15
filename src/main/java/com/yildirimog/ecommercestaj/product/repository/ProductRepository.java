package com.yildirimog.ecommercestaj.product.repository;

import com.yildirimog.ecommercestaj.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
