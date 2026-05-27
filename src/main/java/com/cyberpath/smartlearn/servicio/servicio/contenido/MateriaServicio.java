package com.cyberpath.smartlearn.servicio.servicio.contenido;

import com.cyberpath.smartlearn.modelo.contenido.Materia;

import java.util.List;

public interface MateriaServicio {
    List<Materia> getAll();

    Materia findById(Integer id);

    Materia save(Materia materia);

    void delete(Integer id);

    Materia update(Integer id, Materia materia);

    Long countEjerciciosByMateriaId(Integer idMateria);
}
