package com.cyberpath.smartlearn.servicio.servicio.recurso;

import com.cyberpath.smartlearn.modelo.recurso.TipoRecurso;

import java.util.List;

public interface TipoRecursoServicio {
    List<TipoRecurso> getAll();

    TipoRecurso getById(Integer id);

    TipoRecurso save(TipoRecurso tipoRecurso);

    void delete(Integer id);

    TipoRecurso update(Integer id, TipoRecurso tipoRecurso);
}
