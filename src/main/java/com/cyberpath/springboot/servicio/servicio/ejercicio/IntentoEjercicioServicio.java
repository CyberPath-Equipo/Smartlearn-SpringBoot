package com.cyberpath.springboot.servicio.servicio.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;

import java.util.List;

public interface IntentoEjercicioServicio {
    List<IntentoEjercicio> getAll( );
    IntentoEjercicio getById(Integer id);
    IntentoEjercicio save(IntentoEjercicio intentoEjercicio);
    void delete(Integer id);
    IntentoEjercicio update(Integer id, IntentoEjercicio intentoEjercicio);
}
