package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.CentroDistribucion;
import com.aramdev.delivery.domain.CentroDistribucionErrorCodes;
import com.aramdev.delivery.dto.CentroDistribucionRequest;
import com.aramdev.delivery.dto.CentroDistribucionResponse;
import com.aramdev.delivery.dto.GetCentrosDistribucionRequest;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.CentroDistribucionRepository;
import com.aramdev.delivery.persistence.CentroDistribucionSpecifications;
import com.aramdev.delivery.util.DeletionSummaryResponse;
import com.aramdev.delivery.persistence.JpaSpecificationPaginator;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CentroDistribucionService {

    private static final Map<String, String> SORTS = Map.of(
            "idCentro", "idCentro",
            "nombre", "nombre",
            "ciudad", "ciudad",
            "direccion", "direccion"
    );

    private final CentroDistribucionSpecifications centroDistribucionSpecifications;
    private final CentroDistribucionRepository centroDistribucionRepository;
    private final JpaSpecificationPaginator jpaSpecificationPaginator;

    @Transactional(readOnly = true)
    public OffsetPaginationResponse<CentroDistribucionResponse> getCentrosDistribucion(
            GetCentrosDistribucionRequest filters,
            OffsetPaginationRequest pagination
    ) {
        return jpaSpecificationPaginator.findPage(
                pagination,
                centroDistribucionSpecifications.forRequest(filters),
                centroDistribucionRepository,
                this::toResponse,
                SORTS,
                "idCentro"
        );
    }

    @Transactional(readOnly = true)
    public CentroDistribucionResponse getCentroDistribucionById(Integer id) {
        CentroDistribucion centroDistribucion = centroDistribucionRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO
                ));

        return toResponse(centroDistribucion);
    }

    @Transactional
    public CentroDistribucionResponse createCentroDistribucion(
            CentroDistribucionRequest request
    ) {
        if (centroDistribucionRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new BusinessValidationException(
                    CentroDistribucionErrorCodes.NOMBRE_CENTRO_DUPLICADO
            );
        }

        CentroDistribucion centroDistribucion = new CentroDistribucion();
        centroDistribucion.setNombre(request.nombre());
        centroDistribucion.setCiudad(request.ciudad());
        centroDistribucion.setDireccion(request.direccion());

        return toResponse(centroDistribucionRepository.save(centroDistribucion));
    }

    @Transactional
    public CentroDistribucionResponse updateCentroDistribucion(
            Integer id,
            CentroDistribucionRequest request
    ) {
        CentroDistribucion centroDistribucion = centroDistribucionRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO
                ));

        if (centroDistribucionRepository.existsByNombreIgnoreCaseAndIdCentroNot(
                request.nombre(),
                id
        )) {
            throw new BusinessValidationException(
                    CentroDistribucionErrorCodes.NOMBRE_CENTRO_DUPLICADO
            );
        }

        centroDistribucion.setNombre(request.nombre());
        centroDistribucion.setCiudad(request.ciudad());
        centroDistribucion.setDireccion(request.direccion());

        return toResponse(centroDistribucionRepository.save(centroDistribucion));
    }

    @Transactional
    public DeletionSummaryResponse<Integer> deleteCentrosDistribucion(Set<Integer> ids) {
        Set<Integer> notDeletedIds =
                centroDistribucionRepository.findAllIdsWithRelations(ids);

        ids.removeAll(notDeletedIds);

        if (!ids.isEmpty()) {
            centroDistribucionRepository.deleteAllByIdInBatch(ids);
        }

        return new DeletionSummaryResponse<>(
                ids,
                notDeletedIds
        );
    }

    private CentroDistribucionResponse toResponse(
            CentroDistribucion centroDistribucion
    ) {
        return new CentroDistribucionResponse(
                centroDistribucion.getIdCentro(),
                centroDistribucion.getNombre(),
                centroDistribucion.getCiudad(),
                centroDistribucion.getDireccion()
        );
    }
}