package com.cyberpath.smartlearn.servicio.servicio.ejercicio;

import com.cyberpath.smartlearn.dto.ejercicio.OpcionDto;
import com.cyberpath.smartlearn.modelo.ejercicio.Ejercicio;
import com.cyberpath.smartlearn.modelo.ejercicio.Pregunta;

import java.util.List;

public interface EjercicioServicio {
    List<Ejercicio> getAll();

    Ejercicio findById(Integer id);

    Ejercicio save(Ejercicio ejercicio);

    void delete(Integer id);

    Ejercicio update(Integer id, Ejercicio ejercicio);

    void sincronizarOpciones(Pregunta pregunta, List<OpcionDto> opciones);
}
