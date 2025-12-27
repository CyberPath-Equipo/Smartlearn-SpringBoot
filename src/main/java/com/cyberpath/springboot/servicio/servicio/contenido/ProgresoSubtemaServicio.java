package com.cyberpath.springboot.servicio.servicio.contenido;

import com.cyberpath.springboot.modelo.contenido.ProgresoSubtema;

import java.util.List;

public interface ProgresoSubtemaServicio {
    List<ProgresoSubtema> getAll( );
    ProgresoSubtema getById(Integer id);
    ProgresoSubtema save(ProgresoSubtema progresoSubtema);
    void delete(Integer id);
    ProgresoSubtema update(Integer id, ProgresoSubtema progresoSubtema);
}
