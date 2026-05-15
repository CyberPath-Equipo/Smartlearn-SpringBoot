package com.cyberpath.smartlearn.servicio.servicio.usuario;

import com.cyberpath.smartlearn.modelo.usuario.Rol;

import java.util.List;

public interface RolServicio {
    List<Rol> getAll();

    Rol findById(Integer id);

    Rol save(Rol rol);

    void delete(Integer id);

    Rol update(Integer id, Rol rol);
}
