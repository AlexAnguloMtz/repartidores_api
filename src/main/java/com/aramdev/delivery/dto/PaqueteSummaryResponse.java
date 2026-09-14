package com.aramdev.delivery.dto;

import com.aramdev.delivery.domain.EstadoPaquete;
import com.aramdev.delivery.domain.TamanoEtiqueta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaqueteSummaryResponse(
        Long idPaquete,
        String folio,
        Long idCliente,
        String nombreCliente,
        Integer idCentroOrigen,
        String nombreCentroOrigen,
        String direccionOrigen,
        String direccionDestino,
        BigDecimal latitudDestino,
        BigDecimal longitudDestino,
        BigDecimal pesoKg,
        TamanoEtiqueta tamanoEtiqueta,
        Boolean esPrioritario,
        Boolean esFragil,
        EstadoPaquete estadoActual,
        LocalDateTime fechaCreacion
) {
}