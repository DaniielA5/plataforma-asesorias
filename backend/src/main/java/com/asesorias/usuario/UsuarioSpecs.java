package com.asesorias.usuario;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UsuarioSpecs {

    private UsuarioSpecs() {
    }

    public static Specification<Usuario> filtrar(NombreRol rol, String q) {
        return (root, query, cb) -> {
            List<Predicate> condiciones = new ArrayList<>();

            if (rol != null) {
                condiciones.add(cb.equal(root.get("rol").get("nombre"), rol));
            }

            if (q != null && !q.isBlank()) {
                String patron = "%" + q.trim().toLowerCase() + "%";
                condiciones.add(cb.or(
                        cb.like(cb.lower(root.get("nombre")), patron),
                        cb.like(cb.lower(root.get("correo")), patron)
                ));
            }

            return cb.and(condiciones.toArray(new Predicate[0]));
        };
    }
}