package com.cyberpath.springboot.servicio.servicio.relaciones;

import com.cyberpath.springboot.dto.relaciones.UsuarioMateriaDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateriaId;

import java.util.List;

public interface UsuarioMateriaServicio {
    List<UsuarioMateria> getAll();
    UsuarioMateria getById(UsuarioMateriaId id);
    List<Materia> getMateriasByUser(Integer userId);
    UsuarioMateria save(UsuarioMateria usuarioMateria);
    UsuarioMateria saveReferencia(UsuarioMateriaDto usuarioMateriaDto);
    void delete(UsuarioMateriaId id);
    UsuarioMateria update(UsuarioMateriaId id, UsuarioMateria usuarioMateria);
}
