package com.yildirimog.ecommercestaj.auth.dto;

public record AuthenticationResponse(
        String token,
        Long userId,
        String email,
        String Role
) {
}
