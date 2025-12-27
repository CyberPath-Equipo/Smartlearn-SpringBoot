package com.cyberpath.springboot.servicio.impl.contenido;

import lombok.AllArgsConstructor;
import com.cyberpath.springboot.modelo.contenido.Teoria;
import com.cyberpath.springboot.repositorio.contenido.TeoriaRepositorio;
import com.cyberpath.springboot.servicio.servicio.contenido.TeoriaServicio;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TeoriaImpl implements TeoriaServicio {

    private final TeoriaRepositorio teoriaRepositorio;

    @Override
    public List<Teoria> getAll() {
        return teoriaRepositorio.findAll();
    }

    @Override
    public Teoria getById(Integer id) {
        return teoriaRepositorio.findById(id).orElse(null);
    }

    @Override
    public Teoria save(Teoria teoria) {
        return teoriaRepositorio.save(teoria);
    }

    @Override
    public void delete(Integer id) {
        teoriaRepositorio.deleteById(id);
    }

    @Override
    public Teoria update(Integer id, Teoria teoria) {
        Teoria aux = teoriaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Teoría no encontrada"));

        aux.setContenido(teoria.getContenido());
        aux.setRevisado(teoria.getRevisado());
        aux.setSubtema(teoria.getSubtema());

        return teoriaRepositorio.save(aux);
    }
}