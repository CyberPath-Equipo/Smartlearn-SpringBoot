package com.cyberpath.springboot.servicio.impl.contenido;

import com.cyberpath.springboot.modelo.contenido.ProgresoSubtema;
import com.cyberpath.springboot.repositorio.contenido.ProgresoSubtemaRepositorio;
import com.cyberpath.springboot.servicio.servicio.contenido.ProgresoSubtemaServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProgresoSubtemaImpl implements ProgresoSubtemaServicio {
    private final ProgresoSubtemaRepositorio progresoSubtemaRepositorio;

    @Override
    public List<ProgresoSubtema> getAll() {
        return progresoSubtemaRepositorio.findAll();
    }

    @Override
    public ProgresoSubtema getById(Integer id) {
        return progresoSubtemaRepositorio.findById(id).orElse(null);
    }

    @Override
    public ProgresoSubtema save(ProgresoSubtema progresoSubtema) {
        return progresoSubtemaRepositorio.save(progresoSubtema);
    }

    @Override
    public void delete(Integer id) {
        progresoSubtemaRepositorio.deleteById(id);
    }

    @Override
    public ProgresoSubtema update(Integer id, ProgresoSubtema progresoSubtema) {
        ProgresoSubtema aux = progresoSubtemaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgresoSubtema no encontrado"));
        aux.setTeoriaLeida(progresoSubtema.getTeoriaLeida());
        aux.setEjerciciosCompletados(progresoSubtema.getEjerciciosCompletados());
        aux.setEjerciciosTotales(progresoSubtema.getEjerciciosTotales());
        aux.setPorcentaje(progresoSubtema.getPorcentaje());
        aux.setUltimoAcceso(progresoSubtema.getUltimoAcceso());
        aux.setUsuario(progresoSubtema.getUsuario());
        aux.setSubtema(progresoSubtema.getSubtema());

        return progresoSubtemaRepositorio.save(aux);
    }
}