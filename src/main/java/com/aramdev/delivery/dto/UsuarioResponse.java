package com.aramdev.delivery.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long idUsuario,
        Integer idRol,
        String rol,
        String nombre,
        String email,
        String telefono,
        LocalDateTime fechaRegistro
) {
}