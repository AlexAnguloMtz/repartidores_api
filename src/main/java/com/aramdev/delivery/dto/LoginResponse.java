package com.aramdev.delivery.dto;

public record LoginResponse(
        String email,
        String rol,
        String accessToken
) {
}