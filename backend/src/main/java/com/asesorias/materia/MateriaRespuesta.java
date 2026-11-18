package com.asesorias.materia;

public record MateriaRespuesta(Long id, String codigo, String nombre) {

    public static MateriaRespuesta de(Materia m) {
        return new MateriaRespuesta(m.getId(), m.getCodigo(), m.getNombre());
    }
}