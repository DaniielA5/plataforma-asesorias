package com.asesorias.materia;

import com.asesorias.comun.PaginaRespuesta;
import com.asesorias.error.RecursoNoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asesorias.error.ReglaNegocioException;
import org.springframework.dao.DataIntegrityViolationException;

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


        @Transactional
    public MateriaResponse crear(MateriaRequest peticion) {
        String codigo = peticion.codigo().trim();
        if (materias.existsByCodigo(codigo)) {
            throw new ReglaNegocioException("Ya existe una materia con el código " + codigo);
        }
        Materia materia = new Materia();
        materia.setCodigo(codigo);
        materia.setNombre(peticion.nombre().trim());
        return MateriaResponse.de(materias.save(materia));
    }

    @Transactional
    public MateriaResponse editar(Long id, MateriaRequest peticion) {
        Materia materia = buscar(id);
        String codigo = peticion.codigo().trim();
        if (materias.existsByCodigoAndIdNot(codigo, id)) {
            throw new ReglaNegocioException("Ya existe otra materia con el código " + codigo);
        }
        materia.setCodigo(codigo);
        materia.setNombre(peticion.nombre().trim());
        return MateriaResponse.de(materias.save(materia));
    }

    @Transactional
    public void eliminar(Long id) {
        Materia materia = buscar(id);
        try {
            materias.delete(materia);
            materias.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ReglaNegocioException(
                    "No se puede eliminar la materia porque tiene asesorías registradas");
        }
    }

    private Materia buscar(Long id) {
        return materias.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La materia " + id + " no existe"));
    }
}