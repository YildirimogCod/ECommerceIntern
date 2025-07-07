package com.yildirimog.ecommercestaj.user.dto;

public record UserCreateRequest(
    String firstName,
    String lastName,
    String email,
    String password
) {
}
