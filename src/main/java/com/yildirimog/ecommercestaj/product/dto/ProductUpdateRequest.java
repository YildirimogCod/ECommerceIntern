package com.yildirimog.ecommercestaj.product.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        @NotBlank
        String name,
        String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        int stockQuantity,
        String imageUrl,
        @NotNull(message = "Kategori ID'si boş olamaz")
        Long categoryId

) {
}
