package com.yildirimog.ecommercestaj.product.dto;

import com.yildirimog.ecommercestaj.category.dto.CategoryResponse;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stockQuantity,
        String imageUrl,
        CategoryResponse category
) {
}
