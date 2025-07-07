package com.yildirimog.ecommercestaj.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Ad alanı boş olamaz")
        String firstName,
        @NotBlank(message = "Soyad alanı boş olamaz")
        String lastName,
        @Email(message = "Geçerli bir email giriniz")
        @NotBlank(message = "Email alanı boş olamaz")
        String email,
        @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
        @NotBlank(message = "Şifre alanı boş olamaz")  String password
) {
}
