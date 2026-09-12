package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.CentroDistribucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CentroDistribucionRepository extends
        JpaRepository<CentroDistribucion, Integer>,
        JpaSpecificationExecutor<CentroDistribucion>
{

    @Query(value = """
        select distinct id_centro
        from unidades
        where id_centro in (:ids)
        """, nativeQuery = true)
    Set<Integer> findAllIdsWithRelations(Set<Integer> ids);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCentroNot(String nombre, Integer idCentro);

}