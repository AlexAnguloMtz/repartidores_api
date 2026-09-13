package com.aramdev.delivery.seeder;

import com.aramdev.delivery.domain.CentroDistribucion;
import com.aramdev.delivery.domain.Unidad;
import com.aramdev.delivery.persistence.CentroDistribucionRepository;
import com.aramdev.delivery.persistence.UnidadRepository;
import com.aramdev.delivery.service.UnidadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnidadSeeder {

    private static final String PLACAS_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final UnidadService unidadService;
    private final UnidadRepository unidadRepository;
    private final CentroDistribucionRepository centroDistribucionRepository;

    public void seed() {
        log.debug("Iniciando seeder de unidades");

        if (unidadRepository.count() > 0) {
            log.debug("Ya existen unidades. No se insertará ninguna unidad");
            return;
        }

        log.debug("No existen unidades. Obteniendo centros de distribución");

        List<CentroDistribucion> centros = centroDistribucionRepository.findAll();

        if (centros.isEmpty()) {
            throw new IllegalStateException(
                    "No existen centros de distribución");
        }

        log.debug("Se encontraron {} centros de distribución", centros.size());

        unidadRepository.saveAll(makeAll(centros));

        log.debug("Seeder de unidades completado");
    }

    private List<Unidad> makeAll(List<CentroDistribucion> centros) {
        List<Unidad> unidades = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            CentroDistribucion centro = centros.get(i % centros.size());

            unidades.add(make(
                    "Unidad 0" + i + 1,
                    makePlacas(),
                    centro
            ));
        }

        return unidades;
    }

    private Unidad make(
            String codigoUnidad,
            String placas,
            CentroDistribucion centroDistribucion
    ) {
        Unidad unidad = new Unidad();
        unidad.setCodigoUnidad(codigoUnidad);
        unidad.setPlacas(placas);
        unidad.setCentroDistribucion(centroDistribucion);
        return unidad;
    }

    private String makePlacas() {
        StringBuilder placas = new StringBuilder(12);

        int guion1 = ThreadLocalRandom.current().nextInt(1, 9);
        int guion2 = ThreadLocalRandom.current().nextInt(guion1 + 1, 10);

        for (int i = 0; i < 10; i++) {
            if (i == guion1 || i == guion2) {
                placas.append('-');
            }

            placas.append(
                    PLACAS_CHARS.charAt(
                            ThreadLocalRandom.current().nextInt(PLACAS_CHARS.length())
                    )
            );
        }

        return placas.toString();
    }

}