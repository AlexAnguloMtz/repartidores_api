package com.aramdev.delivery.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SeederCommandLineRunner implements CommandLineRunner {

    private final UsuarioSeeder usuarioSeeder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        usuarioSeeder.seed();
    }

}