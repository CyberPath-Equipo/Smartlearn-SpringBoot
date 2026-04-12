package com.cyberpath.springboot.servicio.impl.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.Opcion;
import com.cyberpath.springboot.repositorio.ejercicio.OpcionRepositorio;
import com.cyberpath.springboot.servicio.servicio.ejercicio.OpcionServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OpcionImpl implements OpcionServicio {
    private final OpcionRepositorio opcionRepositorio;

    @Override
    public List<Opcion> getAll() {
        return opcionRepositorio.findAll();
    }

    @Override
    public Opcion getById(Integer id) {
        return opcionRepositorio.findById(id).orElse(null);
    }

    @Override
    public Opcion save(Opcion opcion) {
        return opcionRepositorio.save(opcion);
    }

    @Override
    public void delete(Integer id) {
        opcionRepositorio.deleteById(id);
    }

    @Override
    public Opcion update(Integer id, Opcion opcion) {
        Opcion aux = opcionRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Opcion no encontrada"));
        aux.setTexto(opcion.getTexto());
        aux.setCorrecta(opcion.getCorrecta());
        aux.setPregunta(opcion.getPregunta());

        return opcionRepositorio.save(aux);
    }
}