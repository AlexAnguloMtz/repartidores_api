package com.aramdev.delivery.service;

import com.aramdev.delivery.dto.LoginRequest;
import com.aramdev.delivery.dto.LoginResponse;
import com.aramdev.delivery.entity.Usuario;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.util.CustomUserDetails;
import com.aramdev.delivery.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Login {

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginResponse run(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        CustomUserDetails userDetails = new CustomUserDetails(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                null,
                List.of()
        );

        String accessToken = jwtUtils.createJwt(userDetails);

        return new LoginResponse(
                usuario.getEmail(),
                usuario.getRol().getNombre(),
                accessToken
        );
    }
}