package com.cyberpath.springboot.servicio.impl.contenido;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.contenido.Materia;
import com.cyberpath.springboot.repositorio.contenido.MateriaRepositorio;
import com.cyberpath.springboot.servicio.contenido.MateriaServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MateriaImpl implements MateriaServicio {
    private final MateriaRepositorio materiaRepositorio;

    @Override
    public List<Materia> getAll() {
        return materiaRepositorio.findAll();
    }

    @Override
    public Materia getById(Integer id) {
        return materiaRepositorio.findById(id).orElse(null);
    }

    @Override
    public Materia save(Materia materia) {
        return materiaRepositorio.save(materia);
    }

    @Override
    public void delete(Integer id) {
        materiaRepositorio.deleteById(id);
    }

    @Override
    public Materia update(Integer id, Materia materia) {
        Materia aux = materiaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        aux.setNombre(materia.getNombre());
        aux.setDescripcion(materia.getDescripcion());

        return materiaRepositorio.save(aux);
    }
}