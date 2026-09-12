package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.UsuarioErrorCodes;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.util.CustomUserDetails;
import com.aramdev.delivery.util.DeletionSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeleteUsuarios {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public DeletionSummaryResponse<Long> run(Set<Long> ids, CustomUserDetails currentUser) {
        if (ids.contains(currentUser.getUserId())) {
            throw new BusinessValidationException(
                    UsuarioErrorCodes.BORRAR_CUENTA_PROPIA_NO_PERMITIDO
            );
        }

        Set<Long> notDeletedIds = usuarioRepository.findAllIdsWithRelations(ids);

        ids.removeAll(notDeletedIds);

        if (!ids.isEmpty()) {
            usuarioRepository.deleteAllByIdInBatch(ids);
        }

        return new DeletionSummaryResponse<>(
                ids,
                notDeletedIds
        );
    }

}