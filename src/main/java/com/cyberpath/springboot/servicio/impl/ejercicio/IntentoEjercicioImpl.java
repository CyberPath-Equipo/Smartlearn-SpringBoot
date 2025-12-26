package com.cyberpath.springboot.servicio.impl.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.IntentoEjercicio;
import com.cyberpath.springboot.repositorio.ejercicio.IntentoEjercicioRepositorio;
import com.cyberpath.springboot.servicio.servicio.ejercicio.IntentoEjercicioServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class IntentoEjercicioImpl implements IntentoEjercicioServicio {
    private final IntentoEjercicioRepositorio intentoEjercicioRepositorio;

    @Override
    public List<IntentoEjercicio> getAll() {
        return intentoEjercicioRepositorio.findAll();
    }

    @Override
    public IntentoEjercicio getById(Integer id) {
        return intentoEjercicioRepositorio.findById(id).orElse(null);
    }

    @Override
    public IntentoEjercicio save(IntentoEjercicio intentoEjercicio) {
        return intentoEjercicioRepositorio.save(intentoEjercicio);
    }

    @Override
    public void delete(Integer id) {
        intentoEjercicioRepositorio.deleteById(id);
    }

    @Override
    public IntentoEjercicio update(Integer id, IntentoEjercicio intentoEjercicio) {
        IntentoEjercicio aux = intentoEjercicioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("IntentoEjercicio no encontrado"));
        aux.setFecha(intentoEjercicio.getFecha());
        aux.setPuntaje(intentoEjercicio.getPuntaje());
        aux.setUsuario(intentoEjercicio.getUsuario());
        aux.setEjercicio(intentoEjercicio.getEjercicio());

        return intentoEjercicioRepositorio.save(aux);
    }
}