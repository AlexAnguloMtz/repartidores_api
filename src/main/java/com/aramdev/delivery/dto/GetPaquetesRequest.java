package com.aramdev.delivery.dto;

import com.aramdev.delivery.domain.EstadoPaquete;
import com.aramdev.delivery.domain.TamanoEtiqueta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class GetPaquetesRequest {

    @Size(max = 20)
    private Set<Long> idPaquete;

    @Size(max = 20)
    private Set<Long> idCliente;

    @Size(max = 20)
    private Set<Integer> idCentroOrigen;

    @Size(max = 20)
    private Set<@Size(max = 50) String> folio;

    @Size(max = 20)
    private Set<@Size(max = 50) String> direccionOrigen;

    @Size(max = 20)
    private Set<@Size(max = 50) String> direccionDestino;

    @DecimalMin(value = "0.01")
    private BigDecimal pesoKgMin;

    @DecimalMin(value = "0.01")
    private BigDecimal pesoKgMax;

    @Size(max = 10)
    private Set<TamanoEtiqueta> tamanoEtiqueta;

    @Size(max = 10)
    private Set<EstadoPaquete> estadoActual;

    private Boolean esPrioritario;

    private Boolean esFragil;

    private LocalDateTime fechaCreacionMin;

    private LocalDateTime fechaCreacionMax;

}