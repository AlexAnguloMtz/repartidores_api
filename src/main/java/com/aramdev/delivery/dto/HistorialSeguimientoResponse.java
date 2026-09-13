package com.aramdev.delivery.dto;

import java.time.LocalDateTime;

public record HistorialSeguimientoResponse(
        Long id,
        String titulo,
        String descripcion,
        LocalDateTime fechaHora
) {
}