package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.Usuario;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UsuarioRepository extends
        JpaRepository<Usuario, Long>,
        JpaSpecificationExecutor<Usuario>
{

    @EntityGraph(attributePaths = {"rol"})
    @NonNull Optional<Usuario> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"rol"})
    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

}