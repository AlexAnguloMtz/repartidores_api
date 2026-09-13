package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.HistorialSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialSeguimientoRepository extends JpaRepository<HistorialSeguimiento, Long> {
}