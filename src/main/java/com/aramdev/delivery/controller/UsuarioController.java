package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.GetUsuariosRequest;
import com.aramdev.delivery.dto.UsuarioCreationRequest;
import com.aramdev.delivery.dto.UsuarioResponse;
import com.aramdev.delivery.dto.UsuarioUpdateRequest;
import com.aramdev.delivery.service.*;
import com.aramdev.delivery.util.CustomUserDetails;
import com.aramdev.delivery.util.DeletionSummaryResponse;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<OffsetPaginationResponse<UsuarioResponse>> getUsuarios(
            @Valid GetUsuariosRequest filters,
            @Valid OffsetPaginationRequest pagination
    ) {
        return ResponseEntity.ok(usuarioService.getUsuarios(filters, pagination));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> createUsuario(
            @Valid @RequestBody UsuarioCreationRequest request
    ) {
        return ResponseEntity.ok(usuarioService.createUsuario(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> getUsuario(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(usuarioService.getUsuario(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request
    ) {
        return ResponseEntity.ok(usuarioService.updateUsuario(id, request));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<DeletionSummaryResponse<Long>> deleteUsuario(
            @RequestParam(name = "idUsuario") Set<Long> ids,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(usuarioService.deleteUsuarios(ids, currentUser));
    }

}