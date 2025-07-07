package com.yildirimog.ecommercestaj.auth.dto;


import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotNull
        @NotEmpty(message = "Ad alanı boş olamaz")
        String firstName,
        @NotNull(message = "Soyad alanı boş olamaz")
        String lastName,
        @Email(message = "Geçerli bir email giriniz")
        @NotBlank(message = "Email alanı boş olamaz")
        String email,
        @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
        @NotBlank(message = "Şifre alanı boş olamaz")
        String password
) {
}
