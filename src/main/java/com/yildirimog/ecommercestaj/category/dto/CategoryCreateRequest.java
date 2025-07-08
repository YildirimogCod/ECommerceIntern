package com.yildirimog.ecommercestaj.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
        @NotBlank(message = "Kategori adı boş olamaz")
        String name,
        String description
) {
}
