package com.aramdev.delivery.domain;

import com.aramdev.delivery.persistence.PGPointType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.Type;
import org.postgresql.geometric.PGpoint;
import org.springframework.data.geo.Point;

@Entity
@Table(name = "paquetes")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long idPaquete;

    private String folio;
    private String direccionOrigen;
    private String direccionDestino;
    @Type(PGPointType.class)
    private PGpoint coordenadasDestino;
    private BigDecimal pesoKg;
    private Boolean esPrioritario;
    private Boolean esFragil;
    private Instant fechaCreacion;

    @Enumerated(EnumType.STRING)
    private TamanoEtiqueta tamanoEtiqueta;

    @Enumerated(EnumType.STRING)
    private EstadoPaquete estadoActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_centro_origen")
    private CentroDistribucion centroOrigen;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paquete other)) {
            return false;
        }
        return idPaquete != null && idPaquete.equals(other.idPaquete);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}