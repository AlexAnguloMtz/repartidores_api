package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.dto.UsuarioResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class UsuarioMapper {

    private final String timezone;

    public UsuarioMapper(
            @Value("${globals.timezone}") String timezone
    ) {
        this.timezone = timezone;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getRol().getIdRol(),
                usuario.getRol().getNombre(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                LocalDateTime.ofInstant(usuario.getFechaRegistro(), ZoneId.of(timezone))
        );
    }

}