package com.cyberpath.springboot.servicio.impl.ejercicio;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.ejercicio.Ejercicio;
import com.cyberpath.springboot.repositorio.ejercicio.EjercicioRepositorio;
import com.cyberpath.springboot.servicio.servicio.ejercicio.EjercicioServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EjercicioImpl implements EjercicioServicio {
    private final EjercicioRepositorio ejercicioRepositorio;

    @Override
    public List<Ejercicio> getAll() {
        return ejercicioRepositorio.findAll();
    }

    @Override
    public Ejercicio getById(Integer id) {
        return ejercicioRepositorio.findById(id).orElse(null);
    }

    @Override
    public Ejercicio save(Ejercicio ejercicio) {
        return ejercicioRepositorio.save(ejercicio);
    }

    @Override
    public void delete(Integer id) {
        ejercicioRepositorio.deleteById(id);
    }

    @Override
    public Ejercicio update(Integer id, Ejercicio ejercicio) {
        Ejercicio aux = ejercicioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
        aux.setNombre(ejercicio.getNombre());
        aux.setHecho(ejercicio.getHecho());
        aux.setSubtema(ejercicio.getSubtema());

        return ejercicioRepositorio.save(aux);
    }
}