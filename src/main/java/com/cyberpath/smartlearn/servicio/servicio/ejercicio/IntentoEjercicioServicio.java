package com.cyberpath.smartlearn.servicio.servicio.ejercicio;

import com.cyberpath.smartlearn.modelo.ejercicio.IntentoEjercicio;

import java.util.List;

public interface IntentoEjercicioServicio {
    List<IntentoEjercicio> getAll();

    IntentoEjercicio findById(Integer id);

    IntentoEjercicio save(IntentoEjercicio intentoEjercicio);

    void delete(Integer id);

    IntentoEjercicio update(Integer id, IntentoEjercicio intentoEjercicio);
}
