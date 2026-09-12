package com.aramdev.delivery.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record GetUnidadesRequest(

        @Size(max = 20)
        List<Integer> idUnidad,

        @Size(max = 20)
        List<Integer> idCentro,

        @Size(max = 20)
        List<@Size(max = 50) String> codigoUnidad,

        @Size(max = 20)
        List<@Size(max = 50) String> placas

) {
}