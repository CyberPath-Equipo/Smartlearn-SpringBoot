package com.cyberpath.springboot.servicio.servicio.recurso;

import com.cyberpath.springboot.modelo.recurso.RecursoAdjunto;

import java.util.List;

public interface RecursoAdjuntoServicio {
    List<RecursoAdjunto> getAll( );
    RecursoAdjunto getById(Integer id);
    RecursoAdjunto save(RecursoAdjunto recursoAdjunto);
    void delete(Integer id);
    RecursoAdjunto update(Integer id, RecursoAdjunto recursoAdjunto);
}
