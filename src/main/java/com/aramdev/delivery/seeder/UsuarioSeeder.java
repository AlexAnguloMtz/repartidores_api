package com.aramdev.delivery.seeder;

import com.aramdev.delivery.domain.Rol;
import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.persistence.RolRepository;
import com.aramdev.delivery.persistence.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioSeeder {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public void seed() {
        log.debug("Iniciando seeder de usuarios");

        if (usuarioRepository.count() > 0) {
            log.debug("Ya existen usuarios. No se insertará ningún usuario");
            return;
        }

        log.debug("No existen usuarios. Obteniendo roles");

        Rol administrador = rolRepository.findByNombre("ADMINISTRADOR")
                .orElseThrow(() -> new IllegalStateException(
                        "Rol ADMINISTRADOR no encontrado"));

        log.debug("Rol ADMINISTRADOR encontrado");

        Rol repartidor = rolRepository.findByNombre("REPARTIDOR")
                .orElseThrow(() -> new IllegalStateException(
                        "Rol REPARTIDOR no encontrado"));

        log.debug("Rol REPARTIDOR encontrado");

        Rol cliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new IllegalStateException(
                        "Rol CLIENTE no encontrado"));

        log.debug("Rol CLIENTE encontrado");

        createUser(
                "ADMINISTRADOR_1@gmail.com",
                "Administrador 1",
                "ADMINISTRADOR123!",
                administrador
        );

        createUser(
                "ADMINISTRADOR_2@gmail.com",
                "Administrador 2",
                "ADMINISTRADOR123!",
                administrador
        );

        createUser(
                "ADMINISTRADOR_3@gmail.com",
                "Administrador 3",
                "ADMINISTRADOR123!",
                administrador
        );

        createUser(
                "REPARTIDOR_1@gmail.com",
                "Repartidor 1",
                "REPARTIDOR123!",
                repartidor
        );

        createUser(
                "REPARTIDOR_2@gmail.com",
                "Repartidor 2",
                "REPARTIDOR123!",
                repartidor
        );

        createUser(
                "REPARTIDOR_3@gmail.com",
                "Repartidor 3",
                "REPARTIDOR123!",
                repartidor
        );

        createUser(
                "CLIENTE_1@gmail.com",
                "Cliente 1",
                "CLIENTE123!",
                cliente
        );

        createUser(
                "CLIENTE_2@gmail.com",
                "Cliente 2",
                "CLIENTE123!",
                cliente
        );

        createUser(
                "CLIENTE_3@gmail.com",
                "Cliente 3",
                "CLIENTE123!",
                cliente
        );

        log.debug("Seeder de usuarios completado");
    }

    private void createUser(
            String email,
            String nombre,
            String password,
            Rol rol) {

        log.debug("Creando usuario {}", email);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNombre(nombre);
        usuario.setPasswordHash(passwordEncoder.encode(password));
        usuario.setRol(rol);
        usuario.setTelefono(String.valueOf(
                ThreadLocalRandom.current().nextLong(
                        1_000_000_000L,
                        10_000_000_000L
                )
        ));
        usuario.setFechaRegistro(Instant.now());

        usuarioRepository.save(usuario);

        log.debug("Usuario {} creado", email);
    }
}