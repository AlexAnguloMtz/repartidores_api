package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.CentroDistribucion;
import com.aramdev.delivery.dto.GetCentrosDistribucionRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CentroDistribucionSpecifications {

    public Specification<CentroDistribucion> forRequest(GetCentrosDistribucionRequest request) {

        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (request.idCentro() != null && !request.idCentro().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("idCentro").in(request.idCentro())
                );
            }

            if (request.nombre() != null && !request.nombre().isEmpty()) {
                var nombrePredicates = cb.disjunction();

                for (String nombre : request.nombre()) {
                    nombrePredicates = cb.or(
                            nombrePredicates,
                            cb.like(
                                    cb.lower(root.get("nombre")),
                                    "%" + nombre.toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                predicates = cb.and(predicates, nombrePredicates);
            }

            if (request.ciudad() != null && !request.ciudad().isEmpty()) {
                var ciudadPredicates = cb.disjunction();

                for (String ciudad : request.ciudad()) {
                    ciudadPredicates = cb.or(
                            ciudadPredicates,
                            cb.like(
                                    cb.lower(root.get("ciudad")),
                                    "%" + ciudad.toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                predicates = cb.and(predicates, ciudadPredicates);
            }

            if (request.direccion() != null && !request.direccion().isEmpty()) {
                var direccionPredicates = cb.disjunction();

                for (String direccion : request.direccion()) {
                    direccionPredicates = cb.or(
                            direccionPredicates,
                            cb.like(
                                    cb.lower(root.get("direccion")),
                                    "%" + direccion.toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                predicates = cb.and(predicates, direccionPredicates);
            }

            return predicates;
        };
    }
}
