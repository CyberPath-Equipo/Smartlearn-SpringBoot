package com.cyberpath.smartlearn.servicio.servicio.relaciones;

import com.cyberpath.smartlearn.dto.relaciones.UsuarioMateriaDto;
import com.cyberpath.smartlearn.modelo.contenido.Materia;
import com.cyberpath.smartlearn.modelo.relaciones.UsuarioMateria;

import java.util.List;

public interface UsuarioMateriaServicio {
    List<UsuarioMateria> getAll();

    UsuarioMateria findById(Integer id);

    List<Materia> getMateriasByUser(Integer userId);

    UsuarioMateria save(UsuarioMateria usuarioMateria);

    UsuarioMateria saveReferencia(UsuarioMateriaDto usuarioMateriaDto);

    void delete(Integer id);

    UsuarioMateria update(Integer id, UsuarioMateria usuarioMateria);
}
