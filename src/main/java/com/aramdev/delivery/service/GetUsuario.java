package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.domain.UsuarioErrorCodes;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetUsuario {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public UsuarioResponse run(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(UsuarioErrorCodes.USUARIO_NO_ENCONTRADO));

        return usuarioMapper.toResponse(usuario);
    }

}