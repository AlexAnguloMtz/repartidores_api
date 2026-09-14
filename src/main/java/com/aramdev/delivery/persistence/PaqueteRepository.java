package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PaqueteRepository extends
        JpaRepository<Paquete, Long>,
        JpaSpecificationExecutor<Paquete>
{
    Optional<Paquete> findByFolioIgnoreCase(String folio);
}