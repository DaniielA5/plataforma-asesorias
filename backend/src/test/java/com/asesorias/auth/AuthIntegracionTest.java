package com.asesorias.auth;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(AuthIntegracionTest.ControladorSoloAdmin.class)
class AuthIntegracionTest {

    private static final String REGISTRO = """
            {"nombre": "Ana Prueba", "correo": "ana.prueba@test.mx",
             "password": "Secreta#123", "telefono": "+525512345678"}
            """;

    @Autowired
    private MockMvc mvc;

    @Test
    void sinTokenEs401() throws Exception {
        mvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Necesitas iniciar sesión"));
    }

    @Test
    void tokenFalsoEs401() throws Exception {
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer abc.def.ghi"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Tu sesión expiró o no es válida"));
    }

    @Test
    void registroYMeConToken() throws Exception {
        String token = registrar();

        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("ana.prueba@test.mx"))
                .andExpect(jsonPath("$.rol").value("ALUMNO"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void rolEquivocadoEs403() throws Exception {
        String tokenAlumno = registrar();

        mvc.perform(get("/api/prueba/solo-admin").header("Authorization", "Bearer " + tokenAlumno))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.estado").value(403));
    }

    @Test
    void despuesDelLogoutElTokenYaNoSirve() throws Exception {
        String token = registrar();

        mvc.perform(post("/api/auth/logout").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginConContrasenaIncorrectaEs401() throws Exception {
        registrar();

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\": \"ana.prueba@test.mx\", \"password\": \"Otra#1234\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Correo o contraseña incorrectos"));
    }

    @Test
    void contrasenaDebilEs400() throws Exception {
        mvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTRO.replace("Secreta#123", "abc")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.password").isNotEmpty());
    }

    @Test
    void correoRepetidoEs422() throws Exception {
        registrar();

        mvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTRO))
                .andExpect(status().isUnprocessableContent());
    }

    private String registrar() throws Exception {
        String respuesta = mvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTRO))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(respuesta, "$.token");
    }

    @RestController
    static class ControladorSoloAdmin {

        @GetMapping("/api/prueba/solo-admin")
        @PreAuthorize("hasRole('ADMIN')")
        String soloAdmin() {
            return "hola admin";
        }
    }
}