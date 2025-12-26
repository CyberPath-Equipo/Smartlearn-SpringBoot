package com.cyberpath.springboot.servicio.servicio.usuario;

import com.cyberpath.springboot.modelo.usuario.UltimaConexion;

import java.util.List;

public interface UltimaConexionServicio {
    List<UltimaConexion> getAll( );
    UltimaConexion getById(Integer id);
    UltimaConexion save(UltimaConexion ultimaConexion);
    void delete(Integer id);
    UltimaConexion update(Integer id, UltimaConexion ultimaConexion);
}
