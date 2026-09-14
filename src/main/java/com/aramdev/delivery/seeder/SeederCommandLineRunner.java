package com.aramdev.delivery.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SeederCommandLineRunner implements CommandLineRunner {

    private final UsuarioSeeder usuarioSeeder;
    private final CentroDistribucionSeeder centroDistribucionSeeder;
    private final UnidadSeeder unidadSeeder;
    private final PaqueteSeeder paqueteSeeder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        usuarioSeeder.seed();
        centroDistribucionSeeder.seed();
        unidadSeeder.seed();
        paqueteSeeder.seed();
    }

}