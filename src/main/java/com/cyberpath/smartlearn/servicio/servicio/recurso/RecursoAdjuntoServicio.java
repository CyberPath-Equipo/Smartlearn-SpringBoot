package com.cyberpath.smartlearn.servicio.servicio.recurso;

import com.cyberpath.smartlearn.modelo.recurso.RecursoAdjunto;

import java.util.List;

public interface RecursoAdjuntoServicio {
    List<RecursoAdjunto> getAll();

    RecursoAdjunto findById(Integer id);

    RecursoAdjunto save(RecursoAdjunto recursoAdjunto);

    void delete(Integer id);

    RecursoAdjunto update(Integer id, RecursoAdjunto recursoAdjunto);
}
