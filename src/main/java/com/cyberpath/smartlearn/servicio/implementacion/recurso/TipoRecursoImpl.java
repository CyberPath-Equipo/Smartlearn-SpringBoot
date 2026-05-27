package com.cyberpath.smartlearn.servicio.implementacion.recurso;

import com.cyberpath.smartlearn.modelo.recurso.TipoRecurso;
import com.cyberpath.smartlearn.repositorio.recurso.TipoRecursoRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.recurso.TipoRecursoServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TipoRecursoImpl implements TipoRecursoServicio {
    private final TipoRecursoRepositorio tipoRecursoRepositorio;

    @Override
    public List<TipoRecurso> getAll() {
        return tipoRecursoRepositorio.findAll();
    }

    @Override
    public TipoRecurso findById(Integer id) {
        return tipoRecursoRepositorio.findById(id).orElse(null);
    }

    @Override
    public TipoRecurso save(TipoRecurso tipoRecurso) {
        return tipoRecursoRepositorio.save(tipoRecurso);
    }

    @Override
    public void delete(Integer id) {
        tipoRecursoRepositorio.deleteById(id);
    }

    @Override
    public TipoRecurso update(Integer id, TipoRecurso tipoRecurso) {
        TipoRecurso aux = tipoRecursoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("TipoRecurso no encontrado"));

        aux.setNombre(tipoRecurso.getNombre());
        aux.setDescripcion(tipoRecurso.getDescripcion());

        return tipoRecursoRepositorio.save(aux);
    }
}