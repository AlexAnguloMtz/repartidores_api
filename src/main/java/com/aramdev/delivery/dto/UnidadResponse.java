package com.aramdev.delivery.dto;

public record UnidadResponse(
        Integer idUnidad,
        String codigoUnidad,
        String placas,
        Integer idCentro,
        String nombreCentro
) {
}