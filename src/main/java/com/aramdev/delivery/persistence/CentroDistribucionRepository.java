package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.CentroDistribucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CentroDistribucionRepository extends
        JpaRepository<CentroDistribucion, Integer>,
        JpaSpecificationExecutor<CentroDistribucion>
{

}