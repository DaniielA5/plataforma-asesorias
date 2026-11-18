package com.asesorias.materia;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController 
@RequestMapping("/api/materias")
public class MateriaController {
    private final MateriaRepository materias;

    public MateriaController(MateriaRepository materias) {
        this.materias = materias;
    }

    @GetMapping 
    public List<MateriaRespuesta> listar() {
        return materias.findAll().stream().map(MateriaRespuesta::de).toList();
    }
}
