package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.Unidad;
import com.aramdev.delivery.dto.GetUnidadesRequest;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UnidadSpecifications {

    public Specification<Unidad> forRequest(GetUnidadesRequest request) {

        return (root, query, cb) -> {

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("centroDistribucion", JoinType.INNER);
            }

            var predicates = cb.conjunction();

            if (request.idUnidad() != null && !request.idUnidad().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("idUnidad").in(request.idUnidad())
                );
            }

            if (request.codigoUnidad() != null && !request.codigoUnidad().isEmpty()) {
                var codigoUnidadPredicates = cb.disjunction();

                for (String codigoUnidad : request.codigoUnidad()) {
                    codigoUnidadPredicates = cb.or(
                            codigoUnidadPredicates,
                            cb.like(
                                    cb.lower(root.get("codigoUnidad")),
                                    "%" + codigoUnidad.toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                predicates = cb.and(predicates, codigoUnidadPredicates);
            }

            if (request.placas() != null && !request.placas().isEmpty()) {
                var placasPredicates = cb.disjunction();

                for (String placas : request.placas()) {
                    placasPredicates = cb.or(
                            placasPredicates,
                            cb.like(
                                    cb.lower(root.get("placas")),
                                    "%" + placas.toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                predicates = cb.and(predicates, placasPredicates);
            }

            if (request.idCentro() != null && !request.idCentro().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("centroDistribucion")
                                .get("idCentro")
                                .in(request.idCentro())
                );
            }

            return predicates;
        };
    }

}