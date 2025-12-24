package com.cyberpath.springboot.servicio.relaciones;

import com.cyberpath.springboot.dto.relaciones.UsuarioMateriaDto;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;

import java.util.List;

public interface UsuarioMateriaServicio {
    List<UsuarioMateria> getAll( );
    UsuarioMateria getById(Integer id);
    List<Materia> getMateriasByUser(Integer userId);
    UsuarioMateria save(UsuarioMateria usuarioMateria);
    UsuarioMateria saveReferencia(UsuarioMateriaDto usuarioMateriaDto);
    void delete(Integer id);
    UsuarioMateria update(Integer id, UsuarioMateria usuarioMateria);
}
