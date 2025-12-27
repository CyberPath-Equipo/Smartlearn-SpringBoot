package com.cyberpath.springboot.servicio.servicio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.Opcion;

import java.util.List;

public interface OpcionServicio {
    List<Opcion> getAll( );
    Opcion getById(Integer id);
    Opcion save(Opcion opcion);
    void delete(Integer id);
    Opcion update(Integer id, Opcion opcion);
}
