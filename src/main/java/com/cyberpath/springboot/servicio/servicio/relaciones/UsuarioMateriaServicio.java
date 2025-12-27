package com.cyberpath.springboot.servicio.servicio.relaciones;

import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;

import java.util.List;

public interface UsuarioMateriaServicio {
    List<UsuarioMateria> getAll( );
    UsuarioMateria getById(Integer id);
    UsuarioMateria save(UsuarioMateria usuarioMateria);
    void delete(Integer id);
    UsuarioMateria update(Integer id, UsuarioMateria usuarioMateria);
}
