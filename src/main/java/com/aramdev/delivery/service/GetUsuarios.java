package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.dto.GetUsuariosRequest;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.persistence.UsuarioSpecifications;
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
    private final PaginationUtils paginationUtils;

    @Transactional(readOnly = true)
    public OffsetPaginationResponse<UsuarioResponse> run(
            GetUsuariosRequest filters,
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

        return paginationUtils.toOffsetPaginationResponse(
                page,
                usuarioMapper::toResponse
        );
    }
    
    private Sort parseSort(String sort) {
        if (!StringUtils.hasText(sort)) {
            return Sort.by(Sort.Direction.ASC, "idUsuario");
        }
        return paginationUtils.parseSortOrThrow(sort, SORTS);
    }

}