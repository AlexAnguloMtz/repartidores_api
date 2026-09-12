package com.aramdev.delivery.persistence;

import com.aramdev.delivery.domain.Usuario;
import com.aramdev.delivery.dto.GetUsuariosRequest;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Locale;

@Component
public class UsuarioSpecifications {

    private final ZoneId timezone;

    public UsuarioSpecifications(@Value("${globals.timezone}") String timezone) {
        this.timezone = ZoneId.of(timezone);
    }

    public Specification<Usuario> forRequest(GetUsuariosRequest request) {

        return (root, query, cb) -> {

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("rol", JoinType.INNER);
            }

            var predicates = cb.conjunction();

            if (request.idUsuario() != null && !request.idUsuario().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("idUsuario").in(request.idUsuario())
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

            if (request.email() != null && !request.email().isEmpty()) {
                var emailPredicates = cb.disjunction();

                for (String email : request.email()) {
                    emailPredicates = cb.or(
                            emailPredicates,
                            cb.like(
                                    cb.lower(root.get("email")),
                                    "%" + email.toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                predicates = cb.and(predicates, emailPredicates);
            }

            if (request.telefono() != null && !request.telefono().isEmpty()) {
                var telefonoPredicates = cb.disjunction();

                for (String telefono : request.telefono()) {
                    telefonoPredicates = cb.or(
                            telefonoPredicates,
                            cb.like(
                                    root.get("telefono"),
                                    "%" + telefono + "%"
                            )
                    );
                }

                predicates = cb.and(predicates, telefonoPredicates);
            }

            if (request.idRol() != null && !request.idRol().isEmpty()) {
                predicates = cb.and(
                        predicates,
                        root.get("rol").get("idRol").in(request.idRol())
                );
            }

            if (request.fechaRegistroMin() != null) {
                predicates = cb.and(
                        predicates,
                        cb.greaterThanOrEqualTo(
                                root.get("fechaRegistro"),
                                request.fechaRegistroMin()
                                        .atZone(timezone)
                                        .toInstant()
                        )
                );
            }

            if (request.fechaRegistroMax() != null) {
                predicates = cb.and(
                        predicates,
                        cb.lessThanOrEqualTo(
                                root.get("fechaRegistro"),
                                request.fechaRegistroMax()
                                        .atZone(timezone)
                                        .toInstant()
                        )
                );
            }

            return predicates;
        };
    }
}
