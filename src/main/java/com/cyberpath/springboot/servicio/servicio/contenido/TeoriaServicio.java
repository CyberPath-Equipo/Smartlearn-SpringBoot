package com.cyberpath.springboot.servicio.servicio.contenido;

import com.cyberpath.springboot.modelo.contenido.Teoria;
import java.util.List;

public interface TeoriaServicio {
    List<Teoria> getAll( );
    Teoria getById(Integer id);
    Teoria save(Teoria teoria);
    void delete(Integer id);
    Teoria update(Integer id, Teoria teoria);
}
