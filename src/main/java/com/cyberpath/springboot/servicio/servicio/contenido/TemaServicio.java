package com.cyberpath.springboot.servicio.servicio.contenido;

import com.cyberpath.springboot.modelo.contenido.Tema;

import java.util.List;

public interface TemaServicio {
    List<Tema> getAll( );
    Tema getById(Integer id);
    Tema save(Tema tema);
    void delete(Integer id);
    Tema update(Integer id, Tema tema);
}
