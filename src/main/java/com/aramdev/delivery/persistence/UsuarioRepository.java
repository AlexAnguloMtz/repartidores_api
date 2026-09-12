package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.Usuario;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UsuarioRepository extends
        JpaRepository<Usuario, Long>,
        JpaSpecificationExecutor<Usuario>
{

    @EntityGraph(attributePaths = {"rol"})
    @NonNull Optional<Usuario> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"rol"})
    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Query(value = """
        select distinct id
        from (
            select id_repartidor as id
            from comprobantes_entrega
            where id_repartidor in (:ids)

            union all

            select id_repartidor as id
            from turnos_repartidor
            where id_repartidor in (:ids)

            union all

            select id_repartidor as id
            from incidencias_entrega
            where id_repartidor in (:ids)

            union all

            select id_cliente as id
            from paquetes
            where id_cliente in (:ids)

            union all

            select id_cliente as id
            from tickets_soporte
            where id_cliente in (:ids)
        ) relations
        """,
            nativeQuery = true
    )
    Set<Long> findAllIdsWithRelations(Set<Long> ids);

}