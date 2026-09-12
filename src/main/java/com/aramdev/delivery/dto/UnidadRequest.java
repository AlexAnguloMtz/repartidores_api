package com.aramdev.delivery.dto;

import com.aramdev.delivery.validation.ValidPlacas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnidadRequest(

        @NotNull
        Integer idCentro,

        @NotBlank
        @ValidPlacas
        String placas

) {
}