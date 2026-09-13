package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.CentroDistribucion;
import com.aramdev.delivery.domain.CentroDistribucionErrorCodes;
import com.aramdev.delivery.domain.Unidad;
import com.aramdev.delivery.domain.UnidadErrorCodes;
import com.aramdev.delivery.dto.GetUnidadesRequest;
import com.aramdev.delivery.dto.UnidadRequest;
import com.aramdev.delivery.dto.UnidadResponse;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.CentroDistribucionRepository;
import com.aramdev.delivery.persistence.UnidadRepository;
import com.aramdev.delivery.persistence.UnidadSpecifications;
import com.aramdev.delivery.util.DeletionSummaryResponse;
import com.aramdev.delivery.persistence.JpaSpecificationPaginator;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UnidadService {

    private static final Map<String, String> SORTS = Map.of(
            "idUnidad", "idUnidad",
            "nombreCentro", "centroDistribucion.nombre",
            "codigoUnidad", "codigoUnidad",
            "placas", "placas"
    );

    private final UnidadSpecifications unidadSpecifications;
    private final UnidadRepository unidadRepository;
    private final CentroDistribucionRepository centroDistribucionRepository;
    private final JpaSpecificationPaginator jpaSpecificationPaginator;

    @Transactional(readOnly = true)
    public OffsetPaginationResponse<UnidadResponse> getUnidades(
            GetUnidadesRequest filters,
            OffsetPaginationRequest pagination
    ) {
        return jpaSpecificationPaginator.findPage(
                pagination,
                unidadSpecifications.forRequest(filters),
                unidadRepository,
                this::toResponse,
                SORTS,
                "idUnidad"
        );
    }

    @Transactional(readOnly = true)
    public UnidadResponse getUnidadById(Integer id) {
        Unidad unidad = unidadRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        UnidadErrorCodes.UNIDAD_NO_ENCONTRADO
                ));

        return toResponse(unidad);
    }

    @Transactional
    public UnidadResponse createUnidad(
            UnidadRequest request
    ) {
        if (unidadRepository.existsByPlacasIgnoreCase(request.placas())) {
            throw new BusinessValidationException(
                    UnidadErrorCodes.PLACAS_DUPLICADAS
            );
        }

        if (unidadRepository.existsByCodigoUnidadIgnoreCase(request.codigoUnidad())) {
            throw new BusinessValidationException(
                    UnidadErrorCodes.CODIGO_UNIDAD_DUPLICADO
            );
        }

        Unidad unidad = new Unidad();

        unidad.setPlacas(request.placas().toUpperCase(Locale.ROOT));
        unidad.setCodigoUnidad(request.codigoUnidad());
        unidad.setCentroDistribucion(
                findCentroDistribucionByIdOrThrow(request.idCentro())
        );

        return toResponse(unidadRepository.save(unidad));
    }

    @Transactional
    public UnidadResponse updateUnidad(
            Integer id,
            UnidadRequest request
    ) {
        Unidad unidad = unidadRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        UnidadErrorCodes.UNIDAD_NO_ENCONTRADO
                ));

        if (unidadRepository.existsByPlacasIgnoreCaseAndIdUnidadNot(
                request.placas(),
                id
        )) {
            throw new BusinessValidationException(
                    UnidadErrorCodes.PLACAS_DUPLICADAS
            );
        }

        if (unidadRepository.existsByCodigoUnidadIgnoreCaseAndIdUnidadNot(
                request.codigoUnidad(),
                id
        )) {
            throw new BusinessValidationException(
                    UnidadErrorCodes.CODIGO_UNIDAD_DUPLICADO
            );
        }

        unidad.setPlacas(request.placas().toUpperCase(Locale.ROOT));
        unidad.setCodigoUnidad(request.codigoUnidad());
        unidad.setCentroDistribucion(
                findCentroDistribucionByIdOrThrow(request.idCentro())
        );

        return toResponse(unidadRepository.save(unidad));
    }

    @Transactional
    public DeletionSummaryResponse<Integer> deleteUnidades(Set<Integer> ids) {
        Set<Integer> notDeletedIds =
                unidadRepository.findAllIdsWithRelations(ids);

        ids.removeAll(notDeletedIds);

        if (!ids.isEmpty()) {
            unidadRepository.deleteAllByIdInBatch(ids);
        }

        return new DeletionSummaryResponse<>(
                ids,
                notDeletedIds
        );
    }

    private UnidadResponse toResponse(Unidad unidad) {
        return new UnidadResponse(
                unidad.getIdUnidad(),
                unidad.getCodigoUnidad(),
                unidad.getPlacas(),
                unidad.getCentroDistribucion().getIdCentro(),
                unidad.getCentroDistribucion().getNombre()
        );
    }

    private CentroDistribucion findCentroDistribucionByIdOrThrow(Integer id) {
        return centroDistribucionRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO
                ));
    }
}