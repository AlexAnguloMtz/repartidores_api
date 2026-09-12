package com.aramdev.delivery.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Integer roleId,
        String role,
        String nombre,
        String email,
        String telefono,
        LocalDateTime creado
) {
}