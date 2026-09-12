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
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import com.aramdev.delivery.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UnidadService {

    private static final String CODIGO_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final Map<String, String> SORTS = Map.of(
            "idUnidad", "idUnidad",
            "nombreCentro", "centroDistribucion.nombre",
            "codigoUnidad", "codigoUnidad",
            "placas", "placas"
    );

    private final UnidadSpecifications unidadSpecifications;
    private final UnidadRepository unidadRepository;
    private final CentroDistribucionRepository centroDistribucionRepository;
    private final PaginationUtils paginationUtils;

    @Transactional(readOnly = true)
    public OffsetPaginationResponse<UnidadResponse> getUnidades(
            GetUnidadesRequest filters,
            OffsetPaginationRequest pagination
    ) {
        PageRequest pageRequest = PageRequest.of(
                pagination.pageNumberOrDefault() - 1,
                pagination.pageSizeOrDefault(),
                parseSort(pagination.sort())
        );

        Page<Unidad> page = unidadRepository.findAll(
                unidadSpecifications.forRequest(filters),
                pageRequest
        );

        return paginationUtils.toOffsetPaginationResponse(
                page,
                this::toResponse
        );
    }

    @Transactional(readOnly = true)
    public UnidadResponse getUnidadById(Integer id) {
        Unidad unidad = unidadRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(UnidadErrorCodes.UNIDAD_NO_ENCONTRADO));

        return toResponse(unidad);
    }

    @Transactional
    public UnidadResponse createUnidad(
            UnidadRequest request
    ) {
        Unidad unidad = new Unidad();
        unidad.setPlacas(request.placas().toUpperCase(Locale.ROOT));
        unidad.setCodigoUnidad(makeCodigoUnidad());
        unidad.setCentroDistribucion(findCentroDistribucionByIdOrThrow(request.idCentro()));

        return toResponse(unidadRepository.save(unidad));
    }

    @Transactional
    public UnidadResponse updateUnidad(
            Integer id,
            UnidadRequest request
    ) {
        Unidad unidad = unidadRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(UnidadErrorCodes.UNIDAD_NO_ENCONTRADO));

        unidad.setPlacas(request.placas().toUpperCase());
        unidad.setCentroDistribucion(findCentroDistribucionByIdOrThrow(request.idCentro()));

        return toResponse(unidadRepository.save(unidad));
    }

    @Transactional
    public void deleteUnidad(Integer id) {
        Unidad unidad = unidadRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(UnidadErrorCodes.UNIDAD_NO_ENCONTRADO));

        unidadRepository.delete(unidad);
    }

    private Sort parseSort(String sort) {
        if (!StringUtils.hasText(sort)) {
            return Sort.by(Sort.Direction.ASC, "idUnidad");
        }
        return paginationUtils.parseSortOrThrow(sort, SORTS);
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
                .orElseThrow(() -> new BusinessValidationException(CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO));
    }

    public String makeCodigoUnidad() {
        StringBuilder codigo = new StringBuilder(20);

        for (int i = 0; i < 16; i++) {
            if (i > 0 && i % 4 == 0) {
                codigo.append('-');
            }

            codigo.append(
                    CODIGO_CHARS.charAt(
                            ThreadLocalRandom.current().nextInt(CODIGO_CHARS.length())
                    )
            );
        }

        return codigo.toString();
    }

}