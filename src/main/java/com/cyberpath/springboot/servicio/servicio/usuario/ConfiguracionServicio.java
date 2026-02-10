package com.cyberpath.springboot.servicio.servicio.usuario;

import com.cyberpath.springboot.modelo.usuario.Configuracion;

import java.util.List;

public interface ConfiguracionServicio {
    List<Configuracion> getAll( );
    Configuracion getById(Integer id);
    Configuracion save(Configuracion configuracion);
    void delete(Integer id);
    Configuracion update(Integer id, Configuracion configuracion);
}
