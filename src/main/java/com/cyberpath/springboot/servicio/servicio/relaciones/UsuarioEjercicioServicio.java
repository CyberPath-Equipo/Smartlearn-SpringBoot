package com.cyberpath.springboot.servicio.servicio.relaciones;

import com.cyberpath.springboot.modelo.relaciones.UsuarioEjercicio;
import com.cyberpath.springboot.modelo.relaciones.UsuarioMateria;

import java.util.List;

public interface UsuarioEjercicioServicio {
    List<UsuarioEjercicio> getAll( );
    UsuarioEjercicio getById(Integer id);
    UsuarioEjercicio save(UsuarioEjercicio usuarioEjercicio);
    void delete(Integer id);
    UsuarioEjercicio update(Integer id, UsuarioEjercicio usuarioEjercicio);
}
