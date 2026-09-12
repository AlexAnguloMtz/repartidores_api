package com.aramdev.delivery.service;

import com.aramdev.delivery.dto.UsuarioRequest;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.domain.Rol;
import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.domain.UsuarioErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.RolRepository;
import com.aramdev.delivery.persistence.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CreateUsuario {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final String timeZoneId;

    public CreateUsuario(
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
    public UsuarioResponse run(UsuarioRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessValidationException(UsuarioErrorCodes.CORREO_DUPLICADO);
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setTelefono(request.telefono());
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setFechaRegistro(Instant.now());
        usuario.setRol(findRoleByIdOrThrow(request.roleId()));

        Usuario saved = usuarioRepository.save(usuario);

        return toResponse(saved);
    }

    private Rol findRoleByIdOrThrow(Integer roleId) {
        return rolRepository.findById(roleId)
                .orElseThrow(() -> new BusinessValidationException(UsuarioErrorCodes.ROL_NO_ENCONTRADO));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getRol().getIdRol(),
                usuario.getRol().getNombre(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                LocalDateTime.ofInstant(usuario.getFechaRegistro(), ZoneId.of(timeZoneId))
        );
    }

}