package com.asesorias.materia;

import com.asesorias.comun.PaginaRespuesta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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


        @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public MateriaResponse crear(@Valid @RequestBody MateriaRequest peticion) {
        return materias.crear(peticion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MateriaResponse editar(@PathVariable Long id, @Valid @RequestBody MateriaRequest peticion) {
        return materias.editar(id, peticion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        materias.eliminar(id);
    }
}