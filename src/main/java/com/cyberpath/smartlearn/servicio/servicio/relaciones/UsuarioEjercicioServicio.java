package com.cyberpath.smartlearn.servicio.servicio.relaciones;

import com.cyberpath.smartlearn.modelo.relaciones.UsuarioEjercicio;

import java.util.List;

public interface UsuarioEjercicioServicio {
    List<UsuarioEjercicio> getAll();

    UsuarioEjercicio findById(Integer id);

    UsuarioEjercicio save(UsuarioEjercicio usuarioEjercicio);

    void delete(Integer id);

    UsuarioEjercicio update(Integer id, UsuarioEjercicio usuarioEjercicio);
}
