package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.*;
import com.aramdev.delivery.service.UnidadService;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unidades")
public class UnidadController {

    private final UnidadService unidadService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<OffsetPaginationResponse<UnidadResponse>> getUnidades(
            @Valid GetUnidadesRequest filters,
            @Valid OffsetPaginationRequest pagination
    ) {
        return ResponseEntity.ok(unidadService.getUnidades(filters, pagination));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UnidadResponse> getUnidad(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(unidadService.getUnidadById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UnidadResponse> createUnidad(
            @Valid @RequestBody UnidadRequest request
    ) {
        return ResponseEntity.ok(unidadService.createUnidad(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UnidadResponse> updateUnidad(
            @PathVariable Integer id,
            @Valid @RequestBody UnidadRequest request
    ) {
        return ResponseEntity.ok(unidadService.updateUnidad(id, request));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> deleteUnidad(
            @RequestParam(name = "idUnidad") List<Integer> ids
    ) {
        unidadService.deleteUnidades(ids);
        return ResponseEntity.ok().build();
    }

}