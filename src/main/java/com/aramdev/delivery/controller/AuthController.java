package com.aramdev.delivery.controller;

import com.aramdev.delivery.dto.LoginRequest;
import com.aramdev.delivery.dto.LoginResponse;
import com.aramdev.delivery.service.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/login")
@RequiredArgsConstructor
public class AuthController {

    private final Login login;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(login.run(request));
    }

}