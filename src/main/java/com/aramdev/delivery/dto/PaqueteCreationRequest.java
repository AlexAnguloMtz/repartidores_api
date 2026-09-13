package com.aramdev.delivery.dto;

import com.aramdev.delivery.domain.TamanoEtiqueta;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PaqueteCreationRequest(

        @NotNull
        Long idCliente,

        @NotNull
        Integer idCentroOrigen,

        @NotBlank
        @Size(max = 255)
        String direccionOrigen,

        @NotBlank
        @Size(max = 255)
        String direccionDestino,

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        BigDecimal latitudDestino,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        BigDecimal longitudDestino,

        @NotNull
        @DecimalMin(value = "0.01")
        @Digits(integer = 4, fraction = 2)
        BigDecimal pesoKg,

        @NotNull
        TamanoEtiqueta tamanoEtiqueta,

        @NotNull
        Boolean esPrioritario,

        @NotNull
        Boolean esFragil

) {
}