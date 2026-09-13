package com.aramdev.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class HistorialSeguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long idHistorial;

    @ToString.Include
    private String titulo;

    @ToString.Include
    private String descripcion;

    @ToString.Include
    private Instant fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paquete")
    private Paquete paquete;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistorialSeguimiento other)) {
            return false;
        }
        return idHistorial != null && idHistorial.equals(other.idHistorial);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}