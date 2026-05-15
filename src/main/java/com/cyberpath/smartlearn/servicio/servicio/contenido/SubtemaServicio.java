package com.cyberpath.smartlearn.servicio.servicio.contenido;

import com.cyberpath.smartlearn.modelo.contenido.Subtema;

import java.util.List;

public interface SubtemaServicio {
    List<Subtema> getAll();

    Subtema getById(Integer id);

    Subtema save(Subtema subtema);

    void delete(Integer id);

    void deleteTeoria(Integer idTeoria);

    Subtema update(Integer id, Subtema subtema);
}
