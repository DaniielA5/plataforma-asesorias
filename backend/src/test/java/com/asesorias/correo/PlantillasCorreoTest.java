package com.asesorias.correo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlantillasCorreoTest {

    private static final String URL = "https://ayudito.juarez.top";

    @Test
    void laBienvenidaSaludaPorElPrimerNombreYEnlazaAlCatalogo() {
        PlantillasCorreo.Contenido c = PlantillasCorreo.bienvenida("Maria Hernandez", "maria@asesorias.com", URL);

        assertTrue(c.html().contains("¡Bienvenido a AyudITO, Maria!"));
        assertTrue(c.html().contains("href=\"" + URL + "/asesorias\""));
        assertTrue(c.texto().contains(URL + "/asesorias"));
        assertFalse(c.html().contains("{{"), "no deben quedar marcadores sin reemplazar");
    }

    @Test
    void elNombreSeEscapaParaQueNoMeteHtmlEnElCorreo() {
        PlantillasCorreo.Contenido c = PlantillasCorreo.bienvenida("<script>alert(1)</script>", "x@asesorias.com", URL);

        assertFalse(c.html().contains("<script>"));
        assertTrue(c.html().contains("&lt;script&gt;"));
    }

    @Test
    void laRecuperacionTraeElEnlaceEnHtmlYEnTexto() {
        String enlace = URL + "/restablecer?token=abc123";
        PlantillasCorreo.Contenido c = PlantillasCorreo.recuperacion(enlace);

        assertTrue(c.html().contains("href=\"" + enlace + "\""));
        assertTrue(c.texto().contains(enlace));
        assertFalse(c.html().contains("{{"));
    }

    /** Escribe los correos en target/correos/ para abrirlos en el navegador y ver el diseño sin enviar nada. */
    @Test
    void generaVistaPrevia() throws IOException {
        Path carpeta = Path.of("target", "correos");
        Files.createDirectories(carpeta);
        Files.writeString(carpeta.resolve("bienvenida.html"),
                PlantillasCorreo.bienvenida("Maria Hernandez", "maria@asesorias.com", URL).html());
        Files.writeString(carpeta.resolve("recuperacion.html"),
                PlantillasCorreo.recuperacion(URL + "/restablecer?token=abc123").html());
    }
}