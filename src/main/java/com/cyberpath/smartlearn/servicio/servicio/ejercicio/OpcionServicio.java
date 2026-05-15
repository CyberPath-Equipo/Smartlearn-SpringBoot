package com.cyberpath.smartlearn.servicio.servicio.ejercicio;

import com.cyberpath.smartlearn.modelo.ejercicio.Opcion;

import java.util.List;

public interface OpcionServicio {
    List<Opcion> getAll();

    Opcion getById(Integer id);

    Opcion save(Opcion opcion);

    void delete(Integer id);

    Opcion update(Integer id, Opcion opcion);
}
