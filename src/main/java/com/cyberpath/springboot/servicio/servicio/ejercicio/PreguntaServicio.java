package com.cyberpath.springboot.servicio.servicio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.Pregunta;

import java.util.List;

public interface PreguntaServicio {
    List<Pregunta> getAll( );
    Pregunta getById(Integer id);
    Pregunta save(Pregunta pregunta);
    void delete(Integer id);
    Pregunta update(Integer id, Pregunta pregunta);
}
