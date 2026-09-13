package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.*;
import com.aramdev.delivery.dto.HistorialSeguimientoResponse;
import com.aramdev.delivery.dto.PaqueteCreationRequest;
import com.aramdev.delivery.dto.PaqueteResponse;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.CentroDistribucionRepository;
import com.aramdev.delivery.persistence.HistorialSeguimientoRepository;
import com.aramdev.delivery.persistence.PaqueteRepository;
import com.aramdev.delivery.persistence.UsuarioRepository;
import org.postgresql.geometric.PGpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaqueteService {

    private final UsuarioRepository usuarioRepository;
    private final CentroDistribucionRepository centroDistribucionRepository;
    private final PaqueteRepository paqueteRepository;
    private final HistorialSeguimientoRepository historialSeguimientoRepository;
    private final ZoneId timezone;

    public PaqueteService(
            UsuarioRepository usuarioRepository,
            CentroDistribucionRepository centroDistribucionRepository,
            PaqueteRepository paqueteRepository,
            HistorialSeguimientoRepository historialSeguimientoRepository,
            @Value("${globals.timezone}") String timezone
    ) {
        this.usuarioRepository = usuarioRepository;
        this.centroDistribucionRepository = centroDistribucionRepository;
        this.paqueteRepository = paqueteRepository;
        this.historialSeguimientoRepository = historialSeguimientoRepository;
        this.timezone = ZoneId.of(timezone);
    }

    @Transactional
    public PaqueteResponse createPaquete(PaqueteCreationRequest request) {
        Usuario cliente = usuarioRepository.findById(request.idCliente())
                .orElseThrow(() -> new BusinessValidationException(
                        ClienteErrorCodes.CLIENTE_NO_ENCONTRADO
                ));

        if (!cliente.getRol().getNombre().equals("CLIENTE")) {
            throw new BusinessValidationException(
                    ClienteErrorCodes.USUARIO_NO_ES_UN_CLIENTE
            );
        }

        CentroDistribucion centroOrigen = centroDistribucionRepository.findById(request.idCentroOrigen())
                .orElseThrow(() -> new BusinessValidationException(
                        CentroDistribucionErrorCodes.CENTRO_NO_ENCONTRADO
                ));

        Paquete paquete = new Paquete();

        paquete.setFolio(makeFolio());
        paquete.setDireccionOrigen(request.direccionOrigen());
        paquete.setDireccionDestino(request.direccionDestino());

        paquete.setCoordenadasDestino(
                new PGpoint(
                        request.longitudDestino().doubleValue(),
                        request.latitudDestino().doubleValue()
                )
        );

        paquete.setPesoKg(request.pesoKg());
        paquete.setTamanoEtiqueta(request.tamanoEtiqueta());
        paquete.setEsPrioritario(request.esPrioritario());
        paquete.setEsFragil(request.esFragil());

        paquete.setEstadoActual(EstadoPaquete.RECIBIDO);
        paquete.setFechaCreacion(Instant.now());

        paquete.setCliente(cliente);
        paquete.setCentroOrigen(centroOrigen);

        paqueteRepository.save(paquete);

        HistorialSeguimiento historial = new HistorialSeguimiento();
        historial.setTitulo(EventoPaquete.RECIBIDO.name());
        historial.setDescripcion("Paquete recibido");
        historial.setFechaHora(Instant.now());

        HistorialSeguimiento savedHistorial = historialSeguimientoRepository.save(historial);

        return toResponse(paquete, List.of(savedHistorial));
    }

    private String makeFolio() {
        int year = Year.now(timezone).getValue();

        int random = ThreadLocalRandom.current()
                .nextInt(100000, 1000000);

        int random2 = ThreadLocalRandom.current()
                .nextInt(10000, 100000);

        return "PK-" + year + "-" + random + "-" + random2;
    }

    private PaqueteResponse toResponse(Paquete paquete, List<HistorialSeguimiento> historial) {
        PGpoint point = paquete.getCoordenadasDestino();

        return new PaqueteResponse(
                paquete.getIdPaquete(),
                paquete.getFolio(),
                paquete.getCliente().getIdUsuario(),
                paquete.getCentroOrigen().getIdCentro(),
                paquete.getDireccionOrigen(),
                paquete.getDireccionDestino(),
                BigDecimal.valueOf(point.y),
                BigDecimal.valueOf(point.x),
                paquete.getPesoKg(),
                paquete.getTamanoEtiqueta(),
                paquete.getEsPrioritario(),
                paquete.getEsFragil(),
                paquete.getEstadoActual(),
                paquete.getFechaCreacion().atZone(timezone).toLocalDateTime(),
                historial.stream().map(this::toResponse).toList()
        );
    }

    private HistorialSeguimientoResponse toResponse(HistorialSeguimiento historial) {
        return new HistorialSeguimientoResponse(
                historial.getIdHistorial(),
                historial.getTitulo(),
                historial.getDescripcion(),
                historial.getFechaHora().atZone(timezone).toLocalDateTime()
        );
    }
}