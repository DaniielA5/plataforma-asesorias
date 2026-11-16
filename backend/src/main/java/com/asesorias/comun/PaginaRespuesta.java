package com.asesorias.comun;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginaRespuesta<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static <T> PaginaRespuesta<T> de(Page<T> pagina) {
        return new PaginaRespuesta<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages()
        );
    }
}