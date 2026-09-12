package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.UsuarioRequest;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.service.CreateUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CreateUsuario createUsuario;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> createUsuario(
            @Valid @RequestBody UsuarioRequest request
    ) {
        return ResponseEntity.ok(createUsuario.run(request));
    }

}