package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.HistorialSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialSeguimientoRepository extends JpaRepository<HistorialSeguimiento, Long> {

    @Query("SELECT h FROM HistorialSeguimiento h WHERE h.paquete.idPaquete = :idPaquete")
    List<HistorialSeguimiento> findAllByIdPaquete(@Param("idPaquete") Long idPaquete);

}