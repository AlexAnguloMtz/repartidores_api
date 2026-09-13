package com.aramdev.delivery.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record GetUsuariosRequest(

        @Size(max = 20)
        List<Long> idUsuario,

        @Size(max = 20)
        List<@Size(max = 100) String> nombre,

        @Size(max = 20)
        List<@Size(max = 100) String> email,

        @Size(max = 20)
        List<@Size(max = 50) String> telefono,

        @Size(max = 5)
        List<Integer> idRol,

        @Size(max = 5)
        List<@Size(max = 30) String> nombreRol,

        LocalDateTime fechaRegistroMin,

        LocalDateTime fechaRegistroMax

) {
}