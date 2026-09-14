package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.Paquete;
import com.aramdev.delivery.dto.GetPaquetesRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Locale;

@Component
public class PaqueteSpecifications {

    private final ZoneId timezone;

    public PaqueteSpecifications(@Value("${globals.timezone}") String timezone) {
        this.timezone = ZoneId.of(timezone);
    }

    public Specification<Paquete> forRequest(GetPaquetesRequest request) {
        return (root, query, cb) -> {

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("cliente");
                root.fetch("centroOrigen");
            }

            Predicate predicates = cb.conjunction();

            if (request == null) {
                return predicates;
            }

            if (request.getIdPaquete() != null && !request.getIdPaquete().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("idPaquete").in(request.getIdPaquete())
                );
            }

            if (request.getIdCliente() != null && !request.getIdCliente().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("cliente").get("idUsuario").in(request.getIdCliente())
                );
            }

            if (request.getIdCentroOrigen() != null && !request.getIdCentroOrigen().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("centroOrigen").get("idCentro").in(request.getIdCentroOrigen())
                );
            }

            if (request.getFolio() != null && !request.getFolio().isEmpty()) {
                var folioPredicates = cb.disjunction();

                for (String folio : request.getFolio()) {
                    if (folio != null && !folio.isBlank()) {
                        folioPredicates = cb.or(
                                folioPredicates,
                                cb.like(
                                        cb.lower(root.get("folio")),
                                        "%" + folio.toLowerCase(Locale.ROOT) + "%"
                                )
                        );
                    }
                }

                predicates = cb.and(predicates, folioPredicates);
            }

            if (request.getDireccionOrigen() != null && !request.getDireccionOrigen().isEmpty()) {
                var direccionOrigenPredicates = cb.disjunction();

                for (String direccionOrigen : request.getDireccionOrigen()) {
                    if (direccionOrigen != null && !direccionOrigen.isBlank()) {
                        direccionOrigenPredicates = cb.or(
                                direccionOrigenPredicates,
                                cb.like(
                                        cb.lower(root.get("direccionOrigen")),
                                        "%" + direccionOrigen.toLowerCase(Locale.ROOT) + "%"
                                )
                        );
                    }
                }

                predicates = cb.and(predicates, direccionOrigenPredicates);
            }

            if (request.getDireccionDestino() != null && !request.getDireccionDestino().isEmpty()) {
                var direccionDestinoPredicates = cb.disjunction();

                for (String direccionDestino : request.getDireccionDestino()) {
                    if (direccionDestino != null && !direccionDestino.isBlank()) {
                        direccionDestinoPredicates = cb.or(
                                direccionDestinoPredicates,
                                cb.like(
                                        cb.lower(root.get("direccionDestino")),
                                        "%" + direccionDestino.toLowerCase(Locale.ROOT) + "%"
                                )
                        );
                    }
                }

                predicates = cb.and(predicates, direccionDestinoPredicates);
            }

            if (request.getPesoKgMin() != null) {
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(
                                root.get("pesoKg"),
                                request.getPesoKgMin()
                        )
                );
            }

            if (request.getPesoKgMax() != null) {
                predicates = cb.and(
                        predicates,
                        cb.lessThanOrEqualTo(
                                root.get("pesoKg"),
                                request.getPesoKgMax()
                        )
                );
            }

            if (request.getTamanoEtiqueta() != null && !request.getTamanoEtiqueta().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("tamanoEtiqueta").in(request.getTamanoEtiqueta())
                );
            }

            if (request.getEstadoActual() != null && !request.getEstadoActual().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("estadoActual").in(request.getEstadoActual())
                );
            }

            if (request.getEsPrioritario() != null) {
                predicates = cb.and(
                        predicates,
                        cb.equal(
                                root.get("esPrioritario"),
                                request.getEsPrioritario()
                        )
                );
            }

            if (request.getEsFragil() != null) {
                predicates = cb.and(
                        predicates,
                        cb.equal(
                                root.get("esFragil"),
                                request.getEsFragil()
                        )
                );
            }

            if (request.getFechaCreacionMin() != null) {
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(
                                root.get("fechaCreacion"),
                                request.getFechaCreacionMin()
                                        .atZone(timezone)
                                        .toInstant()
                        )
                );
            }

            if (request.getFechaCreacionMax() != null) {
                predicates = cb.and(
                        predicates,
                        cb.lessThanOrEqualTo(
                                root.get("fechaCreacion"),
                                request.getFechaCreacionMax()
                                        .atZone(timezone)
                                        .toInstant()
                        )
                );
            }

            return predicates;
        };
    }
}