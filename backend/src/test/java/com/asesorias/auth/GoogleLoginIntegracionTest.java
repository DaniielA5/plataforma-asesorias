package com.asesorias.auth;

import com.asesorias.usuario.UsuarioRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GoogleLoginIntegracionTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsuarioRepository usuarios;

    // Se simula a Google: en una prueba no hay un token real que verificar.
    @MockitoBean
    private VerificadorGoogle verificador;

    @Test
    void cuentaNuevaSeCreaComoAlumnoSinTelefono() throws Exception {
        googleResponde("sub-1", "nueva.google@gmail.com", true);

        entrarConGoogle()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.usuario.rol").value("ALUMNO"))
                .andExpect(jsonPath("$.usuario.telefono").doesNotExist());
    }

    @Test
    void laSegundaVezEntraALaMismaCuenta() throws Exception {
        googleResponde("sub-2", "repite.google@gmail.com", true);

        Integer primera = idDe(entrarConGoogle());
        Integer segunda = idDe(entrarConGoogle());

        assertEquals(primera, segunda);
    }

    @Test
    void correoConfiableSeVinculaYLaContrasenaAnteriorDejaDeServir() throws Exception {
        registrar("vinculo.prueba@gmail.com");
        googleResponde("sub-3", "vinculo.prueba@gmail.com", true);

        entrarConGoogle().andExpect(status().isOk());

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\": \"vinculo.prueba@gmail.com\", \"password\": \"Secreta#123\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void correoNoConfiableSePideContrasena() throws Exception {
        registrar("ajeno.prueba@test.mx");
        googleResponde("sub-4", "ajeno.prueba@test.mx", false);

        entrarConGoogle()
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensaje").value("Ese correo ya tiene cuenta, entra con tu contraseña"));
    }

    @Test
    void otraCuentaDeGoogleConElMismoCorreoSeRechaza() throws Exception {
        googleResponde("sub-5", "doble.prueba@gmail.com", true);
        entrarConGoogle().andExpect(status().isOk());

        googleResponde("sub-distinto", "doble.prueba@gmail.com", true);
        entrarConGoogle().andExpect(status().isUnprocessableContent());
    }

    @Test
    void usuarioDesactivadoNoEntra() throws Exception {
        googleResponde("sub-6", "baja.prueba@gmail.com", true);
        entrarConGoogle().andExpect(status().isOk());
        usuarios.findByCorreo("baja.prueba@gmail.com").orElseThrow().setActivo(false);

        entrarConGoogle().andExpect(status().isUnprocessableContent());
    }

    @Test
    void sinCredencialEs400() throws Exception {
        mvc.perform(post("/api/auth/google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    private void googleResponde(String sub, String correo, boolean confiable) {
        when(verificador.verificar("token-falso"))
                .thenReturn(new CuentaGoogle(sub, correo, "Persona de Prueba", confiable));
    }

    private ResultActions entrarConGoogle() throws Exception {
        return mvc.perform(post("/api/auth/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"credencial\": \"token-falso\"}"));
    }

    private Integer idDe(ResultActions respuesta) throws Exception {
        String cuerpo = respuesta.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return JsonPath.read(cuerpo, "$.usuario.id");
    }

    private void registrar(String correo) throws Exception {
        mvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "Ana Prueba", "correo": "%s",
                                 "password": "Secreta#123", "telefono": "+525512345678"}
                                """.formatted(correo)))
                .andExpect(status().isCreated());
    }
}
