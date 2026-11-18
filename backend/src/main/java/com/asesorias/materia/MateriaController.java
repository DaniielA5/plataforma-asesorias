package com.asesorias.materia;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaService materias;

    public MateriaController(MateriaService materias) {
        this.materias = materias;
    }

    @GetMapping
    public PaginaRespuesta<MateriaResponse> listar(
            @RequestParam(required = false) String q,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable) {
        return materias.listar(q, pageable);
    }

    @GetMapping("/{id}")
    public MateriaResponse ver(@PathVariable Long id) {
        return materias.ver(id);
    }
}