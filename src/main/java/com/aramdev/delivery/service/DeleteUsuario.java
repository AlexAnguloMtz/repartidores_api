package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.domain.UsuarioErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUsuario {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void run(Long id, CustomUserDetails currentUser) {
        if (id.equals(currentUser.getUserId())) {
            throw new BusinessValidationException(
                    UsuarioErrorCodes.BORRAR_CUENTA_PROPIA_NO_PERMITIDO
            );
        }

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new BusinessValidationException(
                        UsuarioErrorCodes.USUARIO_NO_ENCONTRADO
                ));

        usuarioRepository.delete(usuario);
    }

}