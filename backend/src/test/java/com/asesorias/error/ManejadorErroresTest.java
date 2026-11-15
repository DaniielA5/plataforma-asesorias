package com.asesorias.error;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ManejadorErroresTest {

    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = MockMvcBuilders.standaloneSetup(new ControladorDePrueba())
                .setControllerAdvice(new ManejadorErrores())
                .build();
    }

    @ParameterizedTest
    @CsvSource({
            "/prueba/no-autenticado, 401",
            "/prueba/sin-permiso,    403",
            "/prueba/no-encontrado,  404",
            "/prueba/no-existe,      404",
            "/prueba/numero/abc,     400",
            "/prueba/duplicado,      409",
            "/prueba/regla,          422",
            "/prueba/bug,            500"
    })
    void cadaErrorRespondeConSuCodigoYElMismoFormato(String ruta, int estado) throws Exception {
        mvc.perform(get(ruta))
                .andExpect(status().is(estado))
                .andExpect(jsonPath("$.estado").value(estado))
                .andExpect(jsonPath("$.mensaje").isNotEmpty())
                .andExpect(jsonPath("$.ruta").value(ruta))
                .andExpect(jsonPath("$.fecha").exists());
    }

    @Test
    void validacionRegresaElErrorDeCadaCampo() throws Exception {
        mvc.perform(post("/prueba/validacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"\", \"correo\": \"no-es-correo\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombre").isNotEmpty())
                .andExpect(jsonPath("$.errores.correo").isNotEmpty());
    }

    @Test
    void jsonMalEscritoEs400() throws Exception {
        mvc.perform(post("/prueba/validacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ esto no es json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La petición está mal formada"));
    }

    @RestController
    static class ControladorDePrueba {

        record Datos(@NotBlank String nombre, @Email String correo) {
        }

        @PostMapping("/prueba/validacion")
        void validacion(@Valid @RequestBody Datos datos) {
        }

        @GetMapping("/prueba/numero/{id}")
        void numero(@PathVariable Long id) {
        }

        @GetMapping("/prueba/no-autenticado")
        void noAutenticado() {
            throw new BadCredentialsException("contraseña incorrecta");
        }

        @GetMapping("/prueba/sin-permiso")
        void sinPermiso() {
            throw new AccessDeniedException("rol equivocado");
        }

        @GetMapping("/prueba/no-encontrado")
        void noEncontrado() {
            throw new RecursoNoEncontradoException("No existe la asesoría 15");
        }

        @GetMapping("/prueba/duplicado")
        void duplicado() {
            throw new DataIntegrityViolationException("uq_inscripcion_asesoria_alumno");
        }

        @GetMapping("/prueba/regla")
        void regla() {
            throw new ReglaNegocioException("La asesoría ya no tiene cupo");
        }

        @GetMapping("/prueba/bug")
        void bug() {
            throw new IllegalStateException("un bug cualquiera");
        }
    }
}