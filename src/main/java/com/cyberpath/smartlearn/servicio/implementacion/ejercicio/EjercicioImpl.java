package com.cyberpath.smartlearn.servicio.implementacion.ejercicio;

import com.cyberpath.smartlearn.dto.ejercicio.OpcionDto;
import com.cyberpath.smartlearn.modelo.ejercicio.Ejercicio;
import com.cyberpath.smartlearn.modelo.ejercicio.Opcion;
import com.cyberpath.smartlearn.modelo.ejercicio.Pregunta;
import com.cyberpath.smartlearn.repositorio.ejercicio.EjercicioRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.ejercicio.EjercicioServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EjercicioImpl implements EjercicioServicio {
    private final EjercicioRepositorio ejercicioRepositorio;

    @Override
    public List<Ejercicio> getAll() {
        return ejercicioRepositorio.findAll();
    }

    @Override
    public Ejercicio findById(Integer id) {
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
        aux.setTipo(ejercicio.getTipo());
        aux.setDificultad(ejercicio.getDificultad());
        aux.setOrden(ejercicio.getOrden());
        aux.setActivo(ejercicio.getActivo());
        aux.setSubtema(ejercicio.getSubtema());

        return ejercicioRepositorio.save(aux);
    }
}