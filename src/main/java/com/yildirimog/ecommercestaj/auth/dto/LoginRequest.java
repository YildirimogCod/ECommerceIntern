package com.yildirimog.ecommercestaj.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "Geçerli bir email giriniz")
        @NotBlank(message = "Email alanı boş olamaz")
        String email,
        @NotBlank(message = "Şifre alanı boş olamaz")
        String password
) {
}
