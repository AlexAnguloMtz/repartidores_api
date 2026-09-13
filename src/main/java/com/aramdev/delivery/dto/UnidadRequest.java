package com.aramdev.delivery.dto;

import com.aramdev.delivery.validation.ValidPlacas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UnidadRequest(

        @NotNull
        Integer idCentro,

        @NotBlank
        @Size(max = 50)
        String codigoUnidad,

        @NotBlank
        @ValidPlacas
        String placas

) {
}