package com.aramdev.delivery.service;

import com.aramdev.delivery.domain.*;
import com.aramdev.delivery.dto.*;
import com.aramdev.delivery.exception.BusinessValidationException;
import com.aramdev.delivery.persistence.*;
import com.aramdev.delivery.util.CustomUserDetails;
import com.aramdev.delivery.util.OffsetPaginationRequest;
import com.aramdev.delivery.util.OffsetPaginationResponse;
import org.postgresql.geometric.PGpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaqueteService {

    private final Map<String, String> SORTS = Map.of(
            "idPaquete", "idPaquete",
            "folio", "folio",
            "pesoKg", "pesoKg",
            "estadoActual", "estadoActual",
            "fechaCreacion", "fechaCreacion",
            "nombreCliente", "cliente.nombre",
            "nombreCentroOrigen", "centroOrigen.nombre"
    );

    private final UsuarioRepository usuarioRepository;
    private final CentroDistribucionRepository centroDistribucionRepository;
    private final PaqueteRepository paqueteRepository;
    private final HistorialSeguimientoRepository historialSeguimientoRepository;
    private final PaqueteSpecifications paqueteSpecifications;
    private final JpaSpecificationPaginator paginator;
    private final ZoneId timezone;

    public PaqueteService(
            UsuarioRepository usuarioRepository,
            CentroDistribucionRepository centroDistribucionRepository,
            PaqueteRepository paqueteRepository,
            HistorialSeguimientoRepository historialSeguimientoRepository,
            PaqueteSpecifications paqueteSpecifications,
            JpaSpecificationPaginator paginator,
            @Value("${globals.timezone}") String timezone
    ) {
        this.usuarioRepository = usuarioRepository;
        this.centroDistribucionRepository = centroDistribucionRepository;
        this.paqueteRepository = paqueteRepository;
        this.historialSeguimientoRepository = historialSeguimientoRepository;
        this.paqueteSpecifications = paqueteSpecifications;
        this.paginator = paginator;
        this.timezone = ZoneId.of(timezone);
    }

    @Transactional(readOnly = true)
    public PaqueteFullResponse getPaquete(Long id, CustomUserDetails currentUser) {
        Paquete paquete = paqueteRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(PaqueteErrorCodes.PAQUETE_NO_ENCONTRADO));

        if (
                currentUser.hasAuthority("CLIENTE") &&
                !paquete.getCliente().getIdUsuario().equals(currentUser.getUserId())
        ) {
            throw new BusinessValidationException(PaqueteErrorCodes.PAQUETE_NO_ENCONTRADO);
        }

        List<HistorialSeguimiento> historial = historialSeguimientoRepository.findAllByIdPaquete(id);

        return toFullResponse(paquete, historial);
    }

    @GetMapping
    public OffsetPaginationResponse<PaqueteSummaryResponse> getPaquetes(
            GetPaquetesRequest filters,
            OffsetPaginationRequest pagination
    ) {
        return paginator.findPage(
                pagination,
                paqueteSpecifications.forRequest(filters),
                paqueteRepository,
                this::toSummaryResponse,
                SORTS,
                "idPaquete"
        );
    }

    @Transactional
    public PaqueteFullResponse createPaquete(PaqueteCreationRequest request) {
        Instant now = Instant.now();

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
        paquete.setFechaCreacion(now);

        paquete.setCliente(cliente);
        paquete.setCentroOrigen(centroOrigen);

        Paquete savedPaquete = paqueteRepository.save(paquete);

        HistorialSeguimiento historial = new HistorialSeguimiento();
        historial.setTitulo(EventoPaquete.RECIBIDO.name());
        historial.setDescripcion("Paquete recibido");
        historial.setPaquete(savedPaquete);
        historial.setFechaHora(now);

        HistorialSeguimiento savedHistorial = historialSeguimientoRepository.save(historial);

        return toFullResponse(savedPaquete, List.of(savedHistorial));
    }

    private String makeFolio() {
        int year = Year.now(timezone).getValue();

        int random = ThreadLocalRandom.current()
                .nextInt(100000, 1000000);

        int random2 = ThreadLocalRandom.current()
                .nextInt(10000, 100000);

        return "PK-" + year + "-" + random + "-" + random2;
    }

    private PaqueteFullResponse toFullResponse(Paquete paquete, List<HistorialSeguimiento> historial) {
        PGpoint point = paquete.getCoordenadasDestino();

        return new PaqueteFullResponse(
                paquete.getIdPaquete(),
                paquete.getFolio(),
                paquete.getCliente().getIdUsuario(),
                paquete.getCliente().getNombre(),
                paquete.getCentroOrigen().getIdCentro(),
                paquete.getCentroOrigen().getNombre(),
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
                historial.stream().map(this::toFullResponse).toList()
        );
    }

    private PaqueteSummaryResponse toSummaryResponse(Paquete paquete) {
        PGpoint point = paquete.getCoordenadasDestino();

        return new PaqueteSummaryResponse(
                paquete.getIdPaquete(),
                paquete.getFolio(),
                paquete.getCliente().getIdUsuario(),
                paquete.getCliente().getNombre(),
                paquete.getCentroOrigen().getIdCentro(),
                paquete.getCentroOrigen().getNombre(),
                paquete.getDireccionOrigen(),
                paquete.getDireccionDestino(),
                BigDecimal.valueOf(point.y),
                BigDecimal.valueOf(point.x),
                paquete.getPesoKg(),
                paquete.getTamanoEtiqueta(),
                paquete.getEsPrioritario(),
                paquete.getEsFragil(),
                paquete.getEstadoActual(),
                paquete.getFechaCreacion().atZone(timezone).toLocalDateTime()
        );
    }

    private HistorialSeguimientoResponse toFullResponse(HistorialSeguimiento historial) {
        return new HistorialSeguimientoResponse(
                historial.getIdHistorial(),
                historial.getTitulo(),
                historial.getDescripcion(),
                historial.getFechaHora().atZone(timezone).toLocalDateTime()
        );
    }

}