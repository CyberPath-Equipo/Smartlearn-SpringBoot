package com.cyberpath.smartlearn.servicio.servicio.ejercicio;

import com.cyberpath.smartlearn.modelo.ejercicio.Pregunta;

import java.util.List;

public interface PreguntaServicio {
    List<Pregunta> getAll();

    Pregunta findById(Integer id);

    Pregunta save(Pregunta pregunta);

    void delete(Integer id);

    Pregunta update(Integer id, Pregunta pregunta);
}
