package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.UsuarioErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteUsuario {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void run(List<Long> ids, CustomUserDetails currentUser) {
        // TODO
        // Arreglar el error de integridad referencial
        ids.forEach((anId) -> {
            if (currentUser.getUserId().equals(anId)) {
                throw new BusinessValidationException(
                        UsuarioErrorCodes.BORRAR_CUENTA_PROPIA_NO_PERMITIDO
                );
            }
        });

        usuarioRepository.deleteAllById(ids);
    }

}