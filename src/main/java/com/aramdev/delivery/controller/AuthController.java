package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.LoginRequest;
import com.aramdev.delivery.dto.LoginResponse;
import com.aramdev.delivery.dto.RoleResponse;
import com.aramdev.delivery.persistence.RolRepository;
import com.aramdev.delivery.service.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final Login login;
    private final RolRepository rolRepository;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Iterable<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(
                rolRepository.findAll().stream()
                        .map(rol -> new RoleResponse(rol.getIdRol(), rol.getNombre()))
                        .toList()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(login.run(request));
    }

}