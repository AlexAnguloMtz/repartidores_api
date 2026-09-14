package com.aramdev.delivery.seeder;

import com.aramdev.delivery.domain.CentroDistribucion;
import com.aramdev.delivery.domain.TamanoEtiqueta;
import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.dto.PaqueteCreationRequest;
import com.aramdev.delivery.persistence.CentroDistribucionRepository;
import com.aramdev.delivery.persistence.PaqueteRepository;
import com.aramdev.delivery.persistence.UsuarioRepository;
import com.aramdev.delivery.service.PaqueteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaqueteSeeder {

    private final UsuarioRepository usuarioRepository;
    private final PaqueteRepository paqueteRepository;
    private final PaqueteService paqueteService;
    private final CentroDistribucionRepository centroDistribucionRepository;

    public void seed() {
        if (paqueteRepository.count() > 0) {
            log.debug("Ya existen paquetes, se omite la creación");
            return;
        }

        List<Usuario> clientes = usuarioRepository.findAllByRolNombre("CLIENTE");

        if (clientes.isEmpty()) {
            log.debug("No se encontraron clientes para crear paquetes");
            return;
        }

        List<CentroDistribucion> centros = centroDistribucionRepository.findAll();

        if (centros.isEmpty()) {
            log.debug("No se encontraron centros de distribución para crear paquetes");
            return;
        }

        log.debug("Iniciando creación de paquetes");

        for (int i = 0; i < 200; i++) {
            Usuario cliente = clientes.get(
                    ThreadLocalRandom.current().nextInt(clientes.size())
            );

            CentroDistribucion centro = centros.get(
                    ThreadLocalRandom.current().nextInt(centros.size())
            );

            PaqueteCreationRequest request = new PaqueteCreationRequest(
                    cliente.getIdUsuario(),
                    centro.getIdCentro(),
                    randomDireccion(),
                    randomDireccion(),
                    randomDecimal(-90, 90, 6),
                    randomDecimal(-180, 180, 6),
                    randomDecimal(0.01, 99.99, 2),
                    randomTamano(),
                    ThreadLocalRandom.current().nextBoolean(),
                    ThreadLocalRandom.current().nextBoolean()
            );

            paqueteService.createPaquete(request);

            log.debug("Paquetes insertados: {}", i + 1);
        }

        log.debug("Creación de paquetes finalizada");
    }

    private String randomDireccion() {
        String[] calles = {
                "Av. Reforma",
                "Av. Juarez",
                "Calle Morelos",
                "Calle Hidalgo",
                "Av. Constitución",
                "Calle Zaragoza",
                "Av. Revolución",
                "Calle Independencia"
        };

        String[] colonias = {
                "Centro",
                "Industrial",
                "Las Palmas",
                "San Benito",
                "Villa Bonita",
                "Los Pinos",
                "La Esperanza",
                "El Sol"
        };

        return calles[ThreadLocalRandom.current().nextInt(calles.length)]
                + " "
                + ThreadLocalRandom.current().nextInt(10, 999)
                + ", "
                + colonias[ThreadLocalRandom.current().nextInt(colonias.length)];
    }

    private BigDecimal randomDecimal(double min, double max, int decimals) {
        return BigDecimal.valueOf(
                        ThreadLocalRandom.current().nextDouble(min, max)
                )
                .setScale(decimals, RoundingMode.HALF_UP);
    }

    private TamanoEtiqueta randomTamano() {
        TamanoEtiqueta[] values = TamanoEtiqueta.values();

        return values[
                ThreadLocalRandom.current().nextInt(values.length)
                ];
    }
}