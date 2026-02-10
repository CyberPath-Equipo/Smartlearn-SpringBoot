package com.cyberpath.springboot.servicio.impl.contenido;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.contenido.Tema;
import com.cyberpath.springboot.repositorio.contenido.TemaRepositorio;
import com.cyberpath.springboot.servicio.servicio.contenido.TemaServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TemaImpl implements TemaServicio {
    private final TemaRepositorio temaRepositorio;

    @Override
    public List<Tema> getAll() {
        return temaRepositorio.findAll();
    }

    @Override
    public Tema getById(Integer id) {
        return temaRepositorio.findById(id).orElse(null);
    }

    @Override
    public Tema save(Tema tema) {
        return temaRepositorio.save(tema);
    }

    @Override
    public void delete(Integer id) {
        temaRepositorio.deleteById(id);
    }

    @Override
    public Tema update(Integer id, Tema tema) {
        Tema aux = temaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        aux.setNombre(tema.getNombre());
        aux.setMateria(tema.getMateria());

        return temaRepositorio.save(aux);
    }
}