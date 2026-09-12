package com.aramdev.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer idRol;

    @ToString.Include
    private String nombre;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol other)) {
            return false;
        }
        return idRol != null && idRol.equals(other.idRol);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}