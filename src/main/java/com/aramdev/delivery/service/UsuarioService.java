package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.Rol;
import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.domain.UsuarioErrorCodes;
import com.aramdev.delivery.dto.GetUsuariosRequest;
import com.aramdev.delivery.dto.UsuarioCreationRequest;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.dto.UsuarioUpdateRequest;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.RolRepository;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.persistence.UsuarioSpecifications;
import com.aramdev.delivery.util.CustomUserDetails;
import com.aramdev.delivery.util.DeletionSummaryResponse;
import com.aramdev.delivery.persistence.JpaSpecificationPaginator;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

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
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final JpaSpecificationPaginator jpaSpecificationPaginator;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public OffsetPaginationResponse<UsuarioResponse> getUsuarios(
            GetUsuariosRequest filters,
            OffsetPaginationRequest pagination
    ) {
        return jpaSpecificationPaginator.findPage(
                pagination,
                usuarioSpecifications.forRequest(filters),
                usuarioRepository,
                usuarioMapper::toResponse,
                SORTS,
                "idUsuario"
        );
    }

    @Transactional(readOnly = true)
    public UsuarioResponse getUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        UsuarioErrorCodes.USUARIO_NO_ENCONTRADO
                ));

        return usuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse createUsuario(
            UsuarioCreationRequest request
    ) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessValidationException(
                    UsuarioErrorCodes.CORREO_DUPLICADO
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setTelefono(request.telefono());
        usuario.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        usuario.setFechaRegistro(Instant.now());
        usuario.setRol(findRoleByIdOrThrow(request.idRol()));

        Usuario saved = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(saved);
    }

    @Transactional
    public UsuarioResponse updateUsuario(
            Long id,
            UsuarioUpdateRequest request
    ) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        UsuarioErrorCodes.USUARIO_NO_ENCONTRADO
                ));

        if (request.idRol() != null) {
            Rol rol = rolRepository.findById(request.idRol())
                    .orElseThrow(() -> new BusinessValidationException(
                            UsuarioErrorCodes.ROL_NO_ENCONTRADO
                    ));

            usuario.setRol(rol);
        }

        if (StringUtils.hasText(request.nombre())) {
            usuario.setNombre(request.nombre());
        }

        if (StringUtils.hasText(request.email())
                && !request.email().equals(usuario.getEmail())) {

            if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
                throw new BusinessValidationException(
                        UsuarioErrorCodes.CORREO_DUPLICADO
                );
            }

            usuario.setEmail(request.email());
        }

        if (StringUtils.hasText(request.telefono())) {
            usuario.setTelefono(request.telefono());
        }

        if (StringUtils.hasText(request.password())) {
            usuario.setPasswordHash(
                    passwordEncoder.encode(request.password())
            );
        }

        Usuario saved = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(saved);
    }

    @Transactional
    public DeletionSummaryResponse<Long> deleteUsuarios(
            Set<Long> ids,
            CustomUserDetails currentUser
    ) {
        if (ids.contains(currentUser.getUserId())) {
            throw new BusinessValidationException(
                    UsuarioErrorCodes.BORRAR_CUENTA_PROPIA_NO_PERMITIDO
            );
        }

        Set<Long> notDeletedIds =
                usuarioRepository.findAllIdsWithRelations(ids);

        ids.removeAll(notDeletedIds);

        if (!ids.isEmpty()) {
            usuarioRepository.deleteAllByIdInBatch(ids);
        }

        return new DeletionSummaryResponse<>(
                ids,
                notDeletedIds
        );
    }

    private Rol findRoleByIdOrThrow(Integer roleId) {
        return rolRepository.findById(roleId)
                .orElseThrow(() -> new BusinessValidationException(
                        UsuarioErrorCodes.ROL_NO_ENCONTRADO
                ));
    }
}