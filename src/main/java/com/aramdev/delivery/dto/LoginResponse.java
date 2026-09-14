package com.aramdev.delivery.dto;

public record LoginResponse(
        Long idUsuario,
        String email,
        String rol,
        String accessToken
) {
}