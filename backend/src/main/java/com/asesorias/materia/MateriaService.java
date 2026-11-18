package com.asesorias.materia;

import com.asesorias.comun.PaginaRespuesta;
import com.asesorias.error.RecursoNoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MateriaService {

    private final MateriaRepository materias;

    public MateriaService(MateriaRepository materias) {
        this.materias = materias;
    }

    @Transactional(readOnly = true)
    public PaginaRespuesta<MateriaResponse> listar(String q, Pageable pageable) {
        String texto = (q == null) ? "" : q.trim();
        Page<Materia> pagina = materias
                .findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(texto, texto, pageable);
        return PaginaRespuesta.de(pagina.map(MateriaResponse::de));
    }

    @Transactional(readOnly = true)
    public MateriaResponse ver(Long id) {
        return MateriaResponse.de(buscar(id));
    }

    private Materia buscar(Long id) {
        return materias.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La materia " + id + " no existe"));
    }
}