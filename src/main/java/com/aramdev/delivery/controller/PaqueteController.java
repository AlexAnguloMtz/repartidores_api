package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.PaqueteCreationRequest;
import com.aramdev.delivery.dto.PaqueteResponse;
import com.aramdev.delivery.service.PaqueteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/paquetes")
public class PaqueteController {

    private final PaqueteService paqueteService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<PaqueteResponse> createPaquete(
            @Valid @RequestBody PaqueteCreationRequest request
    ) {
        return ResponseEntity.ok(paqueteService.createPaquete(request));
    }

}