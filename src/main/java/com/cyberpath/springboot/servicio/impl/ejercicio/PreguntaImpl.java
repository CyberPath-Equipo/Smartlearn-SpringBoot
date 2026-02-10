package com.cyberpath.springboot.servicio.impl.ejercicio;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.ejercicio.Pregunta;
import com.cyberpath.springboot.repositorio.ejercicio.PreguntaRepositorio;
import com.cyberpath.springboot.servicio.servicio.ejercicio.PreguntaServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PreguntaImpl implements PreguntaServicio {
    private final PreguntaRepositorio preguntaRepositorio;

    @Override
    public List<Pregunta> getAll() {
        return preguntaRepositorio.findAll();
    }

    @Override
    public Pregunta getById(Integer id) {
        return preguntaRepositorio.findById(id).orElse(null);
    }

    @Override
    public Pregunta save(Pregunta pregunta) {
        return preguntaRepositorio.save(pregunta);
    }

    @Override
    public void delete(Integer id) {
        preguntaRepositorio.deleteById(id);
    }

    @Override
    public Pregunta update(Integer id, Pregunta pregunta) {
        Pregunta aux = preguntaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));
        aux.setEnunciado(pregunta.getEnunciado());
        aux.setEjercicio(pregunta.getEjercicio());

        return preguntaRepositorio.save(aux);
    }
}