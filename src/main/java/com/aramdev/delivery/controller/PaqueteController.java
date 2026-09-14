package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.GetPaquetesRequest;
import com.aramdev.delivery.dto.PaqueteCreationRequest;
import com.aramdev.delivery.dto.PaqueteFullResponse;
import com.aramdev.delivery.dto.PaqueteSummaryResponse;
import com.aramdev.delivery.service.PaqueteService;
import com.aramdev.delivery.util.CustomUserDetails;
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
@RequiredArgsConstructor
@RequestMapping("/api/paquetes")
public class PaqueteController {

    private final PaqueteService paqueteService;

    @GetMapping("/{id}")
    public ResponseEntity<PaqueteFullResponse> getPaquete(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(paqueteService.getPaquete(id, currentUser));
    }

    @GetMapping
    public ResponseEntity<OffsetPaginationResponse<PaqueteSummaryResponse>> getPaquetes(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid GetPaquetesRequest filters,
            @Valid OffsetPaginationRequest pagination
    ) {
        if (currentUser.hasAuthority("CLIENTE")) {
            filters.setIdCliente(Set.of(currentUser.getUserId()));
        }

        return ResponseEntity.ok(paqueteService.getPaquetes(filters, pagination));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<PaqueteFullResponse> createPaquete(
            @Valid @RequestBody PaqueteCreationRequest request
    ) {
        return ResponseEntity.ok(paqueteService.createPaquete(request));
    }

}