package com.aramdev.delivery.dto;

public record CentroDistribucionResponse(
        Integer id,
        String nombre,
        String ciudad,
        String direccion
) {
}