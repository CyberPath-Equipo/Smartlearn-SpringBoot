package com.cyberpath.springboot.servicio.servicio.usuario;

import com.cyberpath.springboot.modelo.usuario.Rol;

import java.util.List;

public interface RolServicio {
    List<Rol> getAll( );
    Rol getById(Integer id);
    Rol save(Rol rol);
    void delete(Integer id);
    Rol update(Integer id, Rol rol);
}
