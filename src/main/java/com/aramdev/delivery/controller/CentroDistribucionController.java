package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.CentroDistribucionRequest;
import com.aramdev.delivery.dto.CentroDistribucionResponse;
import com.aramdev.delivery.dto.GetCentrosDistribucionRequest;
import com.aramdev.delivery.service.CentroDistribucionService;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/centros-distribucion")
public class CentroDistribucionController {

    private final CentroDistribucionService centroDistribucionService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<OffsetPaginationResponse<CentroDistribucionResponse>> getCentrosDistribucion(
            @Valid GetCentrosDistribucionRequest filters,
            @Valid OffsetPaginationRequest pagination
    ) {
        return ResponseEntity.ok(centroDistribucionService.getCentrosDistribucion(filters, pagination));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<CentroDistribucionResponse> getCentroDistribucion(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(centroDistribucionService.getCentroDistribucionById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<CentroDistribucionResponse> createCentroDistribucion(
            @Valid @RequestBody CentroDistribucionRequest request
    ) {
        return ResponseEntity.ok(centroDistribucionService.createCentroDistribucion(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<CentroDistribucionResponse> createCentroDistribucion(
            @PathVariable Integer id,
            @Valid @RequestBody CentroDistribucionRequest request
    ) {
        return ResponseEntity.ok(centroDistribucionService.updateCentroDistribucion(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> deleteCentroDistribucion(
            @PathVariable Integer id
    ) {
        centroDistribucionService.deleteCentroDistribucion(id);
        return ResponseEntity.ok().build();
    }

}