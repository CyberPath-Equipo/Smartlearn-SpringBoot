package com.cyberpath.springboot.servicio.ejercicio;

import com.cyberpath.springboot.dto.ejercicio.OpcionDto;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.modelo.ejercicio.Pregunta;

import java.util.List;

public interface EjercicioServicio {
    List<Ejercicio> getAll( );
    Ejercicio getById(Integer id);
    Ejercicio save(Ejercicio ejercicio);
    void delete(Integer id);
    Ejercicio update(Integer id, Ejercicio ejercicio);
    void sincronizarOpciones(Pregunta pregunta, List<OpcionDto> opciones);
}
