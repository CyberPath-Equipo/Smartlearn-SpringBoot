package com.cyberpath.smartlearn.servicio.implementacion.contenido;

import com.cyberpath.smartlearn.modelo.contenido.Subtema;
import com.cyberpath.smartlearn.repositorio.contenido.SubtemaRepositorio;
import com.cyberpath.smartlearn.repositorio.contenido.TeoriaRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.contenido.SubtemaServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SubtemaImpl implements SubtemaServicio {
    private final SubtemaRepositorio subtemaRepositorio;
    private final TeoriaRepositorio teoriaRepositorio;

    @Override
    public List<Subtema> getAll() {
        return subtemaRepositorio.findAll();
    }

    @Override
    public Subtema findById(Integer id) {
        return subtemaRepositorio.findById(id).orElse(null);
    }

    @Override
    public Subtema save(Subtema subtema) {
        return subtemaRepositorio.save(subtema);
    }

    @Override
    public void delete(Integer id) {
        subtemaRepositorio.deleteById(id);
    }

    @Override
    public void deleteTeoria(Integer idTeoria) {
        Subtema subtema = subtemaRepositorio.findById(idTeoria).orElseThrow(() -> new RuntimeException("Subtema no encontrado"));
        teoriaRepositorio.delete(subtema.getTeoria());
        subtema.setTeoria(null);
    }

    @Override
    public Subtema update(Integer id, Subtema subtema) {
        Subtema aux = subtemaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Subtema no encontrado"));
        aux.setNombre(subtema.getNombre());
        aux.setTema(subtema.getTema());

        return subtemaRepositorio.save(aux);
    }
}