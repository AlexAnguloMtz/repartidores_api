package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.GlobalErrorCodes;
import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.dto.GetUsersRequest;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.persistence.UsuarioSpecifications;
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
public class GetUsuarios {

    private static final Map<String, String> SORTS = Map.of(
            "idUsuario", "idUsuario",
            "nombre", "nombre",
            "email", "email",
            "rol", "rol.nombre",
            "telefono", "telefono",
            "fechaRegistro", "fechaRegistro"
    );

    private final UsuarioSpecifications usuarioSpecifications;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PaginationMapper paginationMapper;

    @Transactional(readOnly = true)
    public OffsetPaginationResponse<UsuarioResponse> run(
            GetUsersRequest filters,
            OffsetPaginationRequest pagination
    ) {
        PageRequest pageRequest = PageRequest.of(
                pagination.pageNumberOrDefault() - 1,
                pagination.pageSizeOrDefault(),
                parseSort(pagination.sort())
        );

        Page<Usuario> page = usuarioRepository.findAll(
                usuarioSpecifications.forRequest(filters),
                pageRequest
        );

        return paginationMapper.toOffsetPaginationResponse(
                page,
                usuarioMapper::toResponse
        );
    }
    
    private Sort parseSort(String sort) {
        if (sort == null) {
            return Sort.by(Sort.Direction.ASC, "idUsuario");
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

}