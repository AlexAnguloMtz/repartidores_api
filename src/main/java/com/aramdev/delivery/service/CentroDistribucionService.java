package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.CentroDistribucion;
import com.aramdev.delivery.domain.CentroDistribucionErrorCodes;
import com.aramdev.delivery.domain.GlobalErrorCodes;
import com.aramdev.delivery.dto.CentroDistribucionRequest;
import com.aramdev.delivery.dto.CentroDistribucionResponse;
import com.aramdev.delivery.dto.GetCentrosDistribucionRequest;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.CentroDistribucionRepository;
import com.aramdev.delivery.persistence.CentroDistribucionSpecifications;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import com.aramdev.delivery.util.PaginationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
    private final PaginationMapper paginationMapper;

    @Transactional(readOnly = true)
    public OffsetPaginationResponse<CentroDistribucionResponse> getCentrosDistribucion(
            GetCentrosDistribucionRequest filters,
            OffsetPaginationRequest pagination
    ) {
        PageRequest pageRequest = PageRequest.of(
                pagination.pageNumberOrDefault() - 1,
                pagination.pageSizeOrDefault(),
                parseSort(pagination.sort())
        );

        Page<CentroDistribucion> page = centroDistribucionRepository.findAll(
                centroDistribucionSpecifications.forRequest(filters),
                pageRequest
        );

        return paginationMapper.toOffsetPaginationResponse(
                page,
                this::toResponse
        );
    }

    @Transactional(readOnly = true)
    public CentroDistribucionResponse getCentroDistribucionById(Integer id) {
        CentroDistribucion centroDistribucion = centroDistribucionRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO));

        return toResponse(centroDistribucion);
    }

    @Transactional
    public CentroDistribucionResponse createCentroDistribucion(
            CentroDistribucionRequest request
    ) {
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
                .orElseThrow(() -> new BusinessValidationException(CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO));

        centroDistribucion.setNombre(request.nombre());
        centroDistribucion.setCiudad(request.ciudad());
        centroDistribucion.setDireccion(request.direccion());

        return toResponse(centroDistribucionRepository.save(centroDistribucion));
    }

    @Transactional
    public void deleteCentroDistribucion(Integer id) {
        CentroDistribucion centroDistribucion = centroDistribucionRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO));

        centroDistribucionRepository.delete(centroDistribucion);
    }

    private Sort parseSort(String sort) {
        if (sort == null) {
            return Sort.by(Sort.Direction.ASC, "idCentro");
        }

        int separator = sort.lastIndexOf('-');

        String property = sort.substring(0, separator);
        String direction = sort.substring(separator + 1);

        String mappedProperty = SORTS.get(property);

        if (mappedProperty == null) {
            throw new BusinessValidationException(GlobalErrorCodes.SORT_INVALIDO);
        }

        return Sort.by(
                Sort.Direction.fromString(direction),
                mappedProperty
        );
    }

    private CentroDistribucionResponse toResponse(CentroDistribucion centroDistribucion) {
        return new CentroDistribucionResponse(
                centroDistribucion.getIdCentro(),
                centroDistribucion.getNombre(),
                centroDistribucion.getCiudad(),
                centroDistribucion.getDireccion()
        );
    }

}