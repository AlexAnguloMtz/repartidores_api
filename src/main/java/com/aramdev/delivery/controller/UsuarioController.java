package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.UsuarioCreationRequest;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.dto.UsuarioUpdateRequest;
import com.aramdev.delivery.service.CreateUsuario;
import com.aramdev.delivery.service.DeleteUsuario;
import com.aramdev.delivery.service.GetUsuario;
import com.aramdev.delivery.service.UpdateUsuario;
import com.aramdev.delivery.util.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final GetUsuario getUsuario;
    private final CreateUsuario createUsuario;
    private final UpdateUsuario updateUsuario;
    private final DeleteUsuario deleteUsuario;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> createUsuario(
            @Valid @RequestBody UsuarioCreationRequest request
    ) {
        return ResponseEntity.ok(createUsuario.run(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> getUsuario(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(getUsuario.run(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request
    ) {
        return ResponseEntity.ok(updateUsuario.run(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> deleteUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        deleteUsuario.run(id, currentUser);
        return ResponseEntity.ok().build();
    }

}