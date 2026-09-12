package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.Rol;
import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.domain.UsuarioErrorCodes;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.dto.UsuarioUpdateRequest;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.RolRepository;
import com.aramdev.delivery.persistence.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UpdateUsuario {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final String timeZoneId;

    public UpdateUsuario(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            @Value("${globals.timezone}") String timeZoneId
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.timeZoneId = timeZoneId;
    }

    @Transactional
    public UsuarioResponse run(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(UsuarioErrorCodes.USUARIO_NO_ENCONTRADO));

        if (request.idRol() != null) {
            Rol rol = rolRepository.findById(request.idRol())
                    .orElseThrow(() -> new BusinessValidationException(UsuarioErrorCodes.ROL_NO_ENCONTRADO));

            usuario.setRol(rol);
        }

        if (StringUtils.hasText(request.nombre())) {
            usuario.setNombre(request.nombre());
        }

        if (StringUtils.hasText(request.email()) && !request.email().equals(usuario.getEmail())) {

            if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
                throw new BusinessValidationException(UsuarioErrorCodes.CORREO_DUPLICADO);
            }

            usuario.setEmail(request.email());
        }

        if (StringUtils.hasText(request.telefono())) {
            usuario.setTelefono(request.telefono());
        }

        if (StringUtils.hasText(request.password())) {
            usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        Usuario saved = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                saved.getIdUsuario(),
                saved.getRol().getIdRol(),
                saved.getRol().getNombre(),
                saved.getNombre(),
                saved.getEmail(),
                saved.getTelefono(),
                LocalDateTime.ofInstant(saved.getFechaRegistro(), ZoneId.of(timeZoneId))
        );
    }

}