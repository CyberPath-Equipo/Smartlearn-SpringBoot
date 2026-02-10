package com.cyberpath.springboot.servicio.impl.recurso;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.recurso.RecursoAdjunto;
import com.cyberpath.springboot.repositorio.recurso.RecursoAdjuntoRepositorio;
import com.cyberpath.springboot.servicio.servicio.recurso.RecursoAdjuntoServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RecursoAdjuntoImpl implements RecursoAdjuntoServicio {
    private final RecursoAdjuntoRepositorio recursoAdjuntoRepositorio;

    @Override
    public List<RecursoAdjunto> getAll() {
        return recursoAdjuntoRepositorio.findAll();
    }

    @Override
    public RecursoAdjunto getById(Integer id) {
        return recursoAdjuntoRepositorio.findById(id).orElse(null);
    }

    @Override
    public RecursoAdjunto save(RecursoAdjunto recursoAdjunto) {
        return recursoAdjuntoRepositorio.save(recursoAdjunto);
    }

    @Override
    public void delete(Integer id) {
        recursoAdjuntoRepositorio.deleteById(id);
    }

    @Override
    public RecursoAdjunto update(Integer id, RecursoAdjunto recursoAdjunto) {
        RecursoAdjunto aux = recursoAdjuntoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("RecursoAdjunto no encontrado"));
        aux.setOrden(recursoAdjunto.getOrden());
        aux.setTitulo(recursoAdjunto.getTitulo());
        aux.setUrl(recursoAdjunto.getUrl());
        aux.setDescripcion(recursoAdjunto.getDescripcion());
        aux.setSubtema(recursoAdjunto.getSubtema());
        aux.setTipoRecurso(recursoAdjunto.getTipoRecurso());

        return recursoAdjuntoRepositorio.save(aux);
    }
}