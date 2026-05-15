package com.cyberpath.smartlearn.servicio.servicio.contenido;

import com.cyberpath.smartlearn.modelo.contenido.ProgresoSubtema;

import java.util.List;

public interface ProgresoSubtemaServicio {
    List<ProgresoSubtema> getAll();

    ProgresoSubtema getById(Integer id);

    ProgresoSubtema save(ProgresoSubtema progresoSubtema);

    void delete(Integer id);

    ProgresoSubtema update(Integer id, ProgresoSubtema progresoSubtema);
}
