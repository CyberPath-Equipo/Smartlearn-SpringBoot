package com.cyberpath.springboot.servicio.contenido;

import com.cyberpath.springboot.modelo.contenido.Materia;

import java.util.List;

public interface MateriaServicio {
    List<Materia> getAll( );
    Materia getById(Integer id);
    Materia save(Materia materia);
    void delete(Integer id);
    Materia update(Integer id, Materia materia);
}
