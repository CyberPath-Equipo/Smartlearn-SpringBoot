package com.cyberpath.springboot.servicio.impl.relaciones;

import com.cyberpath.springboot.modelo.relaciones.UsuarioEjercicio;
import com.cyberpath.springboot.repositorio.relaciones.UsuarioEjercicioRepositorio;
import com.cyberpath.springboot.servicio.servicio.relaciones.UsuarioEjercicioServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioEjercicioImpl implements UsuarioEjercicioServicio {
    private final UsuarioEjercicioRepositorio usuarioEjercicioRepositorio;

    @Override
    public List<UsuarioEjercicio> getAll() {
        return usuarioEjercicioRepositorio.findAll();
    }

    @Override
    public UsuarioEjercicio getById(Integer id) {
        return usuarioEjercicioRepositorio.findById(id).orElse(null);
    }

    @Override
    public UsuarioEjercicio save(UsuarioEjercicio usuarioEjercicio) {
        return usuarioEjercicioRepositorio.save(usuarioEjercicio);
    }

    @Override
    public void delete(Integer id) {
        usuarioEjercicioRepositorio.deleteById(id);
    }

    @Override
    public UsuarioEjercicio update(Integer id, UsuarioEjercicio usuarioEjercicio) {
        UsuarioEjercicio aux = usuarioEjercicioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Relacion UsuarioEjercicio no encontrada"));

        return usuarioEjercicioRepositorio.save(aux);
    }
}