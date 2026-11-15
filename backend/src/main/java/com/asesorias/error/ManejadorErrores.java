package com.asesorias.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {

    private static final Logger log = LoggerFactory.getLogger(ManejadorErrores.class);

    // 400 un @Valid @RequestBody  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> datosInvalidos(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError campo : ex.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(campo.getField(), campo.getDefaultMessage());
        }
        return responder(HttpStatus.BAD_REQUEST, "Hay datos inválidos", req, errores);
    }

    // 400 JSON mal escrito
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorRespuesta> peticionMalFormada(Exception ex, HttpServletRequest req) {
        return responder(HttpStatus.BAD_REQUEST, "La petición está mal formada", req, null);
    }

    // 401 falló la autenticación 
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorRespuesta> noAutenticado(AuthenticationException ex, HttpServletRequest req) {
        return responder(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", req, null);
    }

    // 403 @PreAuthorize neg el acceso
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorRespuesta> sinPermiso(AccessDeniedException ex, HttpServletRequest req) {
        return responder(HttpStatus.FORBIDDEN, "No tienes permiso para hacer esto", req, null);
    }

    // 404 el registro que se buscó no existe
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    // 404: la URL no existe
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ErrorRespuesta> rutaInexistente(Exception ex, HttpServletRequest req) {
        return responder(HttpStatus.NOT_FOUND, "La ruta no existe", req, null);
    }

    // 409 la BD rechazp el registro 
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> conflicto(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("Restricción violada en {}: {}", req.getRequestURI(), ex.getMostSpecificCause().getMessage());
        return responder(HttpStatus.CONFLICT, "El registro choca con otro que ya existe", req, null);
    }

    // 422 se rompen una regla del negocio
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorRespuesta> reglaNegocio(ReglaNegocioException ex, HttpServletRequest req) {
        return responder(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), req, null);
    }

     @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> inesperado(Exception ex, HttpServletRequest req) {
        if (ex instanceof ErrorResponse errorDeSpring) {
            return responder(errorDeSpring.getStatusCode(), errorDeSpring.getBody().getDetail(), req, null);
        }
        log.error("Error no controlado en {} {}", req.getMethod(), req.getRequestURI(), ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado", req, null);
    }

    private ResponseEntity<ErrorRespuesta> responder(HttpStatusCode codigo, String mensaje,
                                                     HttpServletRequest req, Map<String, String> errores) {
        HttpStatus estado = HttpStatus.valueOf(codigo.value());
        ErrorRespuesta cuerpo = new ErrorRespuesta(Instant.now(), estado.value(), estado.getReasonPhrase(),
                mensaje, req.getRequestURI(), errores);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}