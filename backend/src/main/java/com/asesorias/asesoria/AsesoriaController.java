package com.asesorias.asesoria;

import com.asesorias.comun.PaginaRespuesta;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asesorias")
public class AsesoriaController {

    private final AsesoriaService asesorias;

    public AsesoriaController(AsesoriaService asesorias) {
        this.asesorias = asesorias;
    }

    @GetMapping
    public PaginaRespuesta<AsesoriaRespuesta> listar(
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) EstadoAsesoria estado,
            Pageable pageable) {
        return asesorias.listar(materiaId, estado, pageable);
    }

    @GetMapping("/mias")
    @PreAuthorize("hasRole('PROFESOR')")
    public PaginaRespuesta<AsesoriaRespuesta> misAsesorias(@AuthenticationPrincipal Jwt jwt, Pageable pageable) {
        return asesorias.listarDelProfesor(Long.valueOf(jwt.getSubject()), pageable);
    }

    @GetMapping("/{id}")
    public AsesoriaRespuesta ver(@PathVariable Long id) {
        return asesorias.ver(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESOR')")
    public AsesoriaRespuesta crear(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody AsesoriaPeticion peticion) {
        return asesorias.crear(Long.valueOf(jwt.getSubject()), peticion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESOR')")
    public AsesoriaRespuesta editar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id,
                                     @Valid @RequestBody AsesoriaPeticion peticion) {
        return asesorias.editar(Long.valueOf(jwt.getSubject()), id, peticion);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('PROFESOR')")
    public AsesoriaRespuesta cancelar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return asesorias.cancelar(Long.valueOf(jwt.getSubject()), id);
    }
}