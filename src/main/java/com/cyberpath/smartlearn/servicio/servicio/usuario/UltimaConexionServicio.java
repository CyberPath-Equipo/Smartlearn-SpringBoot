package com.cyberpath.smartlearn.servicio.servicio.usuario;

import com.cyberpath.smartlearn.modelo.usuario.UltimaConexion;

import java.util.List;

public interface UltimaConexionServicio {
    List<UltimaConexion> getAll();

    UltimaConexion findById(Integer id);

    UltimaConexion save(UltimaConexion ultimaConexion);

    void delete(Integer id);

    UltimaConexion update(Integer id, UltimaConexion ultimaConexion);
}
