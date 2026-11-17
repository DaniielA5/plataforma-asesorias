package com.asesorias.inscripcion;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InscripcionController {

    private final InscripcionService inscripciones;

    public InscripcionController(InscripcionService inscripciones) {
        this.inscripciones = inscripciones;
    }

    @PostMapping("/api/asesorias/{id}/inscripciones")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ALUMNO')")
    public InscritoRespuesta inscribirse(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return inscripciones.inscribirse(Long.valueOf(jwt.getSubject()), id);
    }

    @DeleteMapping("/api/asesorias/{id}/inscripciones")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ALUMNO')")
    public void cancelar(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        inscripciones.cancelarInscripcion(Long.valueOf(jwt.getSubject()), id);
    }

    @GetMapping("/api/asesorias/{id}/inscripciones")
    @PreAuthorize("hasRole('PROFESOR')")
    public List<InscritoRespuesta> inscritos(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return inscripciones.listarInscritos(Long.valueOf(jwt.getSubject()), id);
    }

    @GetMapping("/api/inscripciones/mias")
    @PreAuthorize("hasRole('ALUMNO')")
    public List<MiInscripcionRespuesta> misInscripciones(@AuthenticationPrincipal Jwt jwt) {
        return inscripciones.listarMisInscripciones(Long.valueOf(jwt.getSubject()));
    }
}