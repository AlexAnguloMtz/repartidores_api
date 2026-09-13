package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.Unidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UnidadRepository extends
        JpaRepository<Unidad, Integer>,
        JpaSpecificationExecutor<Unidad>
{

    @Query(value = """
            select distinct id_unidad
            from turnos_repartidor
            where id_unidad in (:ids)
            """,
            nativeQuery = true
    )
    Set<Integer> findAllIdsWithRelations(Set<Integer> ids);

    boolean existsByPlacasIgnoreCase(String placas);

    boolean existsByPlacasIgnoreCaseAndIdUnidadNot(String placas, Integer id);

    boolean existsByCodigoUnidadIgnoreCase(String codigoUnidad);

    boolean existsByCodigoUnidadIgnoreCaseAndIdUnidadNot(String codigoUnidad, Integer id);

}