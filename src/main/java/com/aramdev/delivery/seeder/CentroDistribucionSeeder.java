package com.aramdev.delivery.seeder;

import com.aramdev.delivery.domain.CentroDistribucion;
import com.aramdev.delivery.persistence.CentroDistribucionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CentroDistribucionSeeder {

    private final CentroDistribucionRepository centroDistribucionRepository;

    public void seed() {
        log.debug("Iniciando seeder de centros de distribución");

        if (centroDistribucionRepository.count() > 0) {
            log.debug("Ya existen centros de distribución. No se insertará ningún centro");
            return;
        }

        log.debug("No existen centros de distribución. Creando centros");

        centroDistribucionRepository.saveAll(makeAll());

        log.debug("Seeder de centros de distribución completado");
    }

    private List<CentroDistribucion> makeAll() {
        return List.of(
                make("Centro #001", "Hermosillo", "Blvd. Solidaridad 1250"),
                make("Centro #002", "Hermosillo", "Blvd. Vildósola 840"),
                make("Centro #003", "Hermosillo", "Av. Luis Encinas 530"),
                make("Centro #004", "Nogales", "Av. Tecnológico 1450"),
                make("Centro #005", "Nogales", "Av. Obregón 620"),
                make("Centro #006", "Ciudad Obregón", "Av. Miguel Alemán 920"),
                make("Centro #007", "Ciudad Obregón", "Av. Guerrero 1450"),
                make("Centro #008", "Navojoa", "Blvd. Lázaro Cárdenas 615"),
                make("Centro #009", "Navojoa", "Av. Pesqueira 840"),
                make("Centro #010", "Guaymas", "Av. Serdán 780"),
                make("Centro #011", "Guaymas", "Av. Aquiles Serdán 1450"),
                make("Centro #012", "San Luis Río Colorado", "Av. Obregón 1105"),
                make("Centro #013", "San Luis Río Colorado", "Av. Kino 720"),
                make("Centro #014", "Mexicali", "Blvd. Lázaro Cárdenas 1800"),
                make("Centro #015", "Mexicali", "Calzada Independencia 950"),
                make("Centro #016", "Tijuana", "Blvd. Insurgentes 2250"),
                make("Centro #017", "Tijuana", "Av. Internacional 1180"),
                make("Centro #018", "Ensenada", "Av. Reforma 1450"),
                make("Centro #019", "Chihuahua", "Av. Tecnológico 2100"),
                make("Centro #020", "Chihuahua", "Periférico de la Juventud 3300"),
                make("Centro #021", "Ciudad Juárez", "Av. Ejército Nacional 1850"),
                make("Centro #022", "Ciudad Juárez", "Av. Tecnológico 920"),
                make("Centro #023", "Monterrey", "Av. Universidad 1450"),
                make("Centro #024", "Monterrey", "Av. Eugenio Garza Sada 2800"),
                make("Centro #025", "Saltillo", "Blvd. Venustiano Carranza 1650"),
                make("Centro #026", "Torreón", "Blvd. Independencia 2200"),
                make("Centro #027", "Durango", "Av. Felipe Pescador 1250"),
                make("Centro #028", "Guadalajara", "Av. Circunvalación 1850"),
                make("Centro #029", "Guadalajara", "Av. López de Legazpi 1400"),
                make("Centro #030", "Querétaro", "Av. 5 de Febrero 2100")
        );
    }

    private CentroDistribucion make(
            String nombre,
            String ciudad,
            String direccion
    ) {
        CentroDistribucion centro = new CentroDistribucion();
        centro.setNombre(nombre);
        centro.setCiudad(ciudad);
        centro.setDireccion(direccion);
        return centro;
    }

}
