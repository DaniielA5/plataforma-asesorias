package com.asesorias.notificacion;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class NotificacionSpecs {

    private NotificacionSpecs() {
    }

    public static Specification<Notificacion> filtrar(String canal, String estado) {
        return (root, query, cb) -> {
            List<Predicate> condiciones = new ArrayList<>();
            if (canal != null && !canal.isBlank()) {
                condiciones.add(cb.equal(root.get("canal"), canal.trim().toUpperCase()));
            }
            if (estado != null && !estado.isBlank()) {
                condiciones.add(cb.equal(root.get("estado"), estado.trim().toUpperCase()));
            }
            return cb.and(condiciones.toArray(new Predicate[0]));
        };
    }
}