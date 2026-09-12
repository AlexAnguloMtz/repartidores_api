package com.aramdev.delivery.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record GetCentrosDistribucionRequest(

        @Size(max = 20)
        List<Integer> idCentro,

        @Size(max = 20)
        List<@Size(max = 100) String> nombre,

        @Size(max = 20)
        List<@Size(max = 100) String> ciudad,

        @Size(max = 20)
        List<@Size(max = 100) String> direccion

) {
}