package com.aramdev.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "centros_distribucion")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class CentroDistribucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer idCentro;

    private String nombre;
    private String ciudad;
    private String direccion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CentroDistribucion other)) {
            return false;
        }
        return idCentro != null && idCentro.equals(other.idCentro);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}