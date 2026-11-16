package com.asesorias.comun;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaginaRespuestaTest {

    // 5 elementos en total, páginas de 2: la página 1 (la segunda) trae "c" y "d"
    private final Page<String> pagina = new PageImpl<>(List.of("c", "d"), PageRequest.of(1, 2), 5);

    @Test
    void copiaLosDatosDeLaPagina() {
        PaginaRespuesta<String> respuesta = PaginaRespuesta.de(pagina);

        assertEquals(List.of("c", "d"), respuesta.content());
        assertEquals(1, respuesta.page());
        assertEquals(2, respuesta.size());
        assertEquals(5, respuesta.totalElements());
        assertEquals(3, respuesta.totalPages());
    }

    @Test
    void elJsonTieneLosCamposAcordados() {
        String json = new ObjectMapper().writeValueAsString(PaginaRespuesta.de(pagina));

        assertEquals("{\"content\":[\"c\",\"d\"],\"page\":1,\"size\":2,\"totalElements\":5,\"totalPages\":3}", json);
    }


    //ejemplo 
    /*
    @GetMapping
    public PaginaRespuesta<MateriaRespuesta> listar(@RequestParam(required = false) String q, Pageable pageable) {
        return PaginaRespuesta.de(materias.buscar(q, pageable).map(MateriaRespuesta::de));
    }
     */
}
