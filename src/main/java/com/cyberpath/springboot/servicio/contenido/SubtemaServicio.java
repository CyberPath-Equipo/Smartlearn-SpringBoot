package com.cyberpath.springboot.servicio.contenido;

import com.cyberpath.springboot.modelo.contenido.Subtema;

import java.util.List;

public interface SubtemaServicio {
    List<Subtema> getAll( );
    Subtema getById(Integer id);
    Subtema save(Subtema subtema);
    void delete(Integer id);
    Subtema update(Integer id, Subtema subtema);
}
