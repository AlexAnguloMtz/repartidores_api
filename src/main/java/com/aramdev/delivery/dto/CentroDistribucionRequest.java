package com.aramdev.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CentroDistribucionRequest(

        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotBlank
        @Size(max = 100)
        String ciudad,

        @NotBlank
        @Size(max = 255)
        String direccion

) {
}