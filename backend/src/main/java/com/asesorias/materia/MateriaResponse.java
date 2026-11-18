package com.asesorias.materia;

public record MateriaResponse(Long id, String codigo, String nombre) {

    public static MateriaResponse de(Materia materia) {
        return new MateriaResponse(materia.getId(), materia.getCodigo(), materia.getNombre());
    }
}