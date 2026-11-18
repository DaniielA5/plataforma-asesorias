package com.asesorias.materia;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AsignacionMateriasRequest(
        @NotNull(message = "La lista de materias es obligatoria")
        List<Long> materiaIds
) {
}