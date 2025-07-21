package com.yildirimog.ecommercestaj.product.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public record ProductUpdateRequest(
        @NotBlank
                String name,
        String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        int stockQuantity,
        String imageUrl,
        @NotEmpty(message = "Kategori ID listesi boş olamaz")
        Set<Long> categoryIds

        ) {
        }
