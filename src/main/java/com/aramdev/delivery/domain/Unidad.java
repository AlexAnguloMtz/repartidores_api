package com.aramdev.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "unidades")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer idUnidad;

    @ToString.Include
    private String codigoUnidad;

    private String placas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_centro")
    private CentroDistribucion centroDistribucion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unidad other)) {
            return false;
        }
        return idUnidad != null && idUnidad.equals(other.idUnidad);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}